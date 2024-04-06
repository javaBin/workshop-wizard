package backend.repository

import backend.dto.WorkshopDTO
import kotlinx.serialization.Contextual
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class Workshop(
    override val id: Int,
    val title: String,
    val description: String,
    val startTime: Instant,
    val endTime: Instant,
    val capacity: Int
) : Model {
    fun toDTO() = WorkshopDTO(title, description, startTime, endTime, capacity)
}

class WorkshopRepository {
    internal object WorkshopTable : IntIdTable() {
        val title = varchar("title", 128)
        val description = varchar("description", 256)
        val startTime = timestamp("start_time")
        val endTime = timestamp("end_time")
        val capacity = integer("capacity")

        fun toModel(it: ResultRow) = Workshop(
            it[id].value,
            it[title],
            it[description],
            it[startTime],
            it[endTime],
            it[capacity]
        )
    }
    fun list(): List<Workshop> {
        return transaction {
            WorkshopTable.selectAll()
                .map(WorkshopTable::toModel)
        }
    }
}
