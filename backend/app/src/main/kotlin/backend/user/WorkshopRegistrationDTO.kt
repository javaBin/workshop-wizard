package backend.user

import kotlinx.serialization.Serializable

@Serializable
data class WorkshopRegistrationDTO(
    val id: Int,
    val user: UserDTO,
    val workshop: WorkshopDTO,
    var state: String,
)
