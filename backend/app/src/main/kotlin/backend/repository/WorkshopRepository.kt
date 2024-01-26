package backend.repository

import backend.model.Workshop


class WorkshopRepository {
    var map = mutableMapOf<Int, Workshop>(
        1 to Workshop(1, "Kotlin", "John Doe"),
        2 to Workshop(2, "Ktor", "Jane Doe"),
        3 to Workshop(3, "Kotlin Multiplatform", "John Doe")
    )


    fun getAll(): List<Workshop> {
        return map.values.toList()
    }

    fun getById(id: Int): Workshop? {
        return map[id]
    }

    fun add(workshop: Workshop) {
        val maxId = map.keys.maxOrNull()?.plus(1) ?: 1
        map[maxId] = workshop
    }

    fun update(workshop: Workshop) {
        map[workshop.id] = workshop
    }
}
