package backend.repository

import backend.dto.ProviderDTO
import com.inventy.plugins.DatabaseFactory.Companion.dbQuery
import kotlinx.serialization.SerialName
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select

enum class ProviderType (val id: Int) {
    @SerialName("auth0")
    AUTH0(0),
    @SerialName("google-oauth2")
    GOOGLE(1),
    @SerialName("facebook")
    FACEBOOK(2);
    companion object {
        fun fromId(id: Int): ProviderType {
            entries.forEach {
                if (it.id == id) {
                    return it
                }
            }
            throw IllegalArgumentException("No AuthProvider with id $id found")
        }
    }
}
class Provider(
    override val id: Int,
    val userId: Int,
    val providerType: ProviderType,
    val providerId: String,
) : Model {
    fun toDTO(): ProviderDTO {
        return ProviderDTO(
            userId,
            providerType,
            providerId,
        )
    }
}

class ProviderRepository {
    private object ProviderTable : IntIdTable() {
        val userId = reference("userId", UserRepository.UserTable.id)
        val providerType = enumerationByName<ProviderType>("provider_type", 64)
        val providerId = varchar("provider_id", 256)

        fun toModel(it: ResultRow) = Provider(
            it[id].value,
            it[userId].value,
            it[providerType],
            it[providerId],
        )
    }

    suspend fun create(provider: ProviderDTO): Int = dbQuery {
        ProviderTable.insertAndGetId {
            it[userId] = provider.userId
            it[providerType] = provider.providerType
            it[providerId] = provider.providerId
        }.value
    }



    suspend fun getByUserId(userId: Int): List<Provider> {
        return dbQuery {
            ProviderTable.select { ProviderTable.userId eq userId }
                .map(ProviderTable::toModel)
        }
    }

    suspend fun updateProvidersForUser(userId: Int, providers: List<ProviderDTO>) {
        val existingProviders = getByUserId(userId)
        val providersToAdd = providers.filter {
            existingProviders.none { provider -> provider.providerType == it.providerType }
        }
        providersToAdd.forEach {
            create(ProviderDTO( userId, it.providerType, it.providerId))
        }
    }
}
