package backend.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkshopDTO(val title: String, val description: String)

