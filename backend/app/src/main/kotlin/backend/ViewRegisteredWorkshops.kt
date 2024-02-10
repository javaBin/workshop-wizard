package backend

import backend.model.WorkshopRegistration
import backend.repository.UserRepository

class ViewRegisteredWorkshops(private val userRepository: UserRepository) {
    fun viewRegisteredWorkshops(userId: Int): List<WorkshopRegistration> {
        val workshops = userRepository.getWorkShopRegistrations(userId)
        // Legg til funksjonalitet for å vise hvilke workshops brukeren har venteliste på
        return workshops
    }
}
