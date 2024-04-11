package backend.repository

import backend.dto.WorkshopDTO
import backend.dto.WorkshopImport
import backend.dto.WorkshopListImportDTO
import com.inventy.plugins.DatabaseFactory.Companion.dbQuery
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

class Workshop(
    override val id: Int,
    val external_id: String,
    val title: String,
    val description: String,
    val startTime: Instant,
    val endTime: Instant,
    val capacity: Int = 30
) : Model {
    fun toDTO() = WorkshopDTO(title, description, startTime, endTime, capacity)
}

class WorkshopRepository {
    internal object WorkshopTable : IntIdTable() {
        val external_id = varchar("external_id", 64)
        val title = varchar("title", 264)
        val description = varchar("description", 256)
        val startTime = timestamp("start_time")
        val endTime = timestamp("end_time")
        val capacity = integer("capacity")

        fun toModel(it: ResultRow) = Workshop(
            id = it[id].value,
            external_id = it[external_id],
            title = it[title],
            description = it[description],
            startTime = it[startTime],
            endTime = it[endTime],
            capacity = it[capacity],
        )
    }

    suspend fun list(): List<Workshop> = dbQuery {
        WorkshopTable.selectAll()
            .map(WorkshopTable::toModel)
    }

    suspend fun create(workshop: WorkshopImport) = dbQuery {
        WorkshopTable.insert {
            it[external_id] = workshop.id
            it[title] = workshop.title
            it[description] =
                workshop.abstract.take(250) + if (workshop.abstract.length > 250) " ..." else "" // Todo change to varchar 2000?
            it[startTime] = Instant.parse(workshop.startTimeZulu)
            it[endTime] = Instant.parse(workshop.endTimeZulu)
            it[capacity] = 30
        }
    }

    suspend fun create(workshops: WorkshopListImportDTO) = dbQuery {
        WorkshopTable.batchInsert(workshops.sessions) { workshop ->
            this[WorkshopTable.external_id] = workshop.id
            this[WorkshopTable.title] = workshop.title
            this[WorkshopTable.description] =
                workshop.abstract.take(250) + if (workshop.abstract.length > 250) " ..." else ""
            this[WorkshopTable.startTime] = Instant.parse(workshop.startTimeZulu)
            this[WorkshopTable.endTime] = Instant.parse(workshop.endTimeZulu)
            this[WorkshopTable.capacity] = 30 // Or use the actual capacity if available
        }
    }
}
