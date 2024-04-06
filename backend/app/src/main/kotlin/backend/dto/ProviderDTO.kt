package backend.dto

import backend.repository.ProviderType
import kotlinx.serialization.Serializable

@Serializable
data class ProviderDTO(
    val userId: Int,
    val providerType: ProviderType,
    val providerId: String
) : DTO
