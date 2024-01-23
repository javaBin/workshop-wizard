package backend

import backend.domain.WorkshopRegistration
import backend.user.UserRepository

class RegisteredWorkshops(private val userRepository: UserRepository) {
    fun viewRegisteredWorkshops(userId: Int): List<WorkshopRegistration> {
        val workshops = userRepository.getWorkShopRegistrations(userId)
        return workshops
    }
}
