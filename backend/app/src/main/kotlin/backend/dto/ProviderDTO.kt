package backend.dto

import backend.model.ProviderType
import kotlinx.serialization.Serializable

@Serializable
data class ProviderDTO(override val id: Long? = null, val providerType: ProviderType, val providerId: String) : DTO
