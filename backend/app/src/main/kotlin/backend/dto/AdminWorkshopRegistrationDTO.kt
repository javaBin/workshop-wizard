package backend.dto

import backend.model.WorkshopRegistrationState
import kotlinx.serialization.Serializable

@Serializable
data class AdminWorkshopRegistrationDTO(val firstName: String, val lastName: String, val email: String, val state: WorkshopRegistrationState)
