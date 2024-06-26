package backend.repository

import backend.dto.SpeakerDTO
import backend.dto.WorkshopDTO
import backend.dto.WorkshopImport
import backend.util.TimeUtil
import com.inventy.plugins.DatabaseFactory.Companion.dbQuery
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

data class Workshop(
    val id: String,
    val title: String,
    val description: String,
    val startTime: Instant,
    val endTime: Instant,
    val capacity: Int,
    val active: Boolean
) {
    fun toDTO(speakerDTOS: List<SpeakerDTO>?) = WorkshopDTO(
        title = title,
        description = description,
        startTime = TimeUtil.toGmtPlus2(startTime),
        endTime = TimeUtil.toGmtPlus2(endTime),
        capacity = capacity,
        active = active,
        speakers = speakerDTOS ?: listOf()
    )
}

class WorkshopRepository {

    companion object {
        private const val WORKSHOP_CAPACITY = 30
    }

    internal object WorkshopTable : Table() {

        val id = varchar("id", 64)
        val title = varchar("title", 256)
        val description = varchar("description", 2048)
        val startTime = timestamp("start_time")
        val endTime = timestamp("end_time")
        val capacity = integer("capacity")
        val active = bool("active")

        override val primaryKey = PrimaryKey(arrayOf(id), "id")

        fun toModel(it: ResultRow) = Workshop(
            id = it[id],
            title = it[title],
            description = it[description],
            startTime = it[startTime],
            endTime = it[endTime],
            capacity = it[capacity],
            active = it[active],
        )
    }

    suspend fun list(): List<Workshop> = dbQuery {
        WorkshopTable.selectAll()
            .map(WorkshopTable::toModel)
    }


    private suspend fun upsertActive(workshops: List<WorkshopImport>) = dbQuery {
        WorkshopTable.batchUpsert(workshops) { workshop ->
            this[WorkshopTable.id] = workshop.id
            this[WorkshopTable.title] = workshop.title
            this[WorkshopTable.description] =
                workshop.abstract.take(250) + if (workshop.abstract.length > 250) " ..." else ""
            this[WorkshopTable.startTime] = Instant.parse(workshop.startTimeZulu)
            this[WorkshopTable.endTime] = Instant.parse(workshop.endTimeZulu)
            this[WorkshopTable.capacity] = WORKSHOP_CAPACITY
            this[WorkshopTable.active] = true
        }
    }

    private suspend fun upsert(workshops: List<Workshop>) = dbQuery {
        WorkshopTable.batchUpsert(workshops) { workshop ->
            this[WorkshopTable.id] = workshop.id
            this[WorkshopTable.title] = workshop.title
            this[WorkshopTable.description] = workshop.description
            this[WorkshopTable.startTime] = workshop.startTime
            this[WorkshopTable.endTime] = workshop.endTime
            this[WorkshopTable.capacity] = WORKSHOP_CAPACITY
            this[WorkshopTable.active] = workshop.active
        }
    }

    private suspend fun setWorkshopsToDisabled(activeWorkshops: List<WorkshopImport>) {
        val activeWorkshopsIds = activeWorkshops.map { it.id }
        val allDisabledList = list()
            .filterNot { it.id in activeWorkshopsIds }
            .map { it.copy(active = false) }
        val t = upsert(allDisabledList)
    }

    suspend fun update(workshops: List<WorkshopImport>) {

        dbQuery {
            setWorkshopsToDisabled(workshops)
            upsertActive(workshops)

        }
    }
}
