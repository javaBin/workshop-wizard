package backend.model

import backend.dto.WorkshopDTO
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
class Workshop(
    override val id: Int,
    val title: String,
    val description: String,
    @Contextual
    val startTime: Instant,
    @Contextual
    val endTime: Instant,
    val capacity: Int
    ) : Model {
    constructor(id: Int, title: String, description: String) : this(id, title, description, Instant.now(),  Instant.now(), 30)
    fun toDTO() = WorkshopDTO(title, description)

}
