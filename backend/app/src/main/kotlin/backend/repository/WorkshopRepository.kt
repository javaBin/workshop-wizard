package backend.repository

import backend.model.Workshop
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


class WorkshopRepository {
    private object WorkshopTable : IntIdTable() {
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
