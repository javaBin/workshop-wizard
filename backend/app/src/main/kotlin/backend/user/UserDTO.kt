package backend.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(val id: Int, val firstName: String, val lastName: String, val email: String)
