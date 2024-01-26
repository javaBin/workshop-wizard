package backend.repository

import backend.config.TestData
import backend.dto.ProviderDTO
import backend.dto.UserDTO
import backend.model.Provider
import backend.model.User
import backend.model.WorkshopRegistration
import backend.model.WorkshopRegistrationState

class UserRepository{
    val userMap = TestData.userMap
    val registrationMap = TestData.registrationMap
    val workshopMap = TestData.workshopMap

    fun create(user: UserDTO): User {
        val maxId = userMap.keys.max() + 1
        userMap.put(maxId, User(
            maxId,
            user.firstName,
            user.lastName,
            user.email,
            user.imageUrl,
            mutableListOf(),
        ))
        return userMap[maxId]!!
    }

    fun getUsers(): List<User> {
        return userMap.values.toList()
    }

    fun readByEmail(email: String): User? {
        return userMap.values.firstOrNull { it.email == email }
    }

    fun updateProviders(userId: Int, existingProviders: List<Provider>, providers: List<ProviderDTO>) {
        // add provider
        providers.filter {
            existingProviders.none { provider -> provider.providerType == it.providerType.id }
        }.forEach(
            fun (provider: ProviderDTO) {
                val maxId = userMap[userId]!!.providers.maxOf { it.id!! } + 1
                userMap[userId]!!.providers += Provider(
                    maxId,
                    userId,
                    provider.providerType.id,
                    provider.providerId,
                )
            }
        )
    }

    fun findProviderById(providerId: String): Provider? {
        return userMap.values.flatMap { it.providers }.firstOrNull { it.providerId == providerId }
    }
    fun getWorkShopRegistrations(userId: Int): List<WorkshopRegistration> {
        return registrationMap.filter {
            it.value.user.id == userId
        }.values.toList()
    }

    fun addWorkshopRegistrations(
        userId: Int,
        workshopId: Int,
    ) {
        workshopMap.get(workshopId) ?: throw RuntimeException("Workshop does not exist")
        val maxId = registrationMap.keys.max() + 1
        registrationMap.put(
            maxId,
            WorkshopRegistration(
                maxId,
                userMap[userId]!!,
                workshopMap[workshopId]!!,
                WorkshopRegistrationState.PENDING,
            ),
        )
    }

    fun cancelWorkshopRegistration(
        userId: Int,
        workshopId: Int,
    ) {
        val registration =
            registrationMap.filter { it.value.user.id == userId && it.value.workshop.id == workshopId }
                .values.firstOrNull() ?: throw RuntimeException("Workshop registration does not exist")
        registration.state = WorkshopRegistrationState.CANCELED
    }
}
