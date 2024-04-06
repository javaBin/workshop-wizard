package backend.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class WorkshopDTO(
    val title: String,
    val description: String,
    @Contextual
    val startTime: Instant,
    @Contextual
    val endTime: Instant,
    val capacity: Int
)

