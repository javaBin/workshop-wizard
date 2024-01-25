package backend

import backend.domain.User
import backend.domain.Workshop
import backend.domain.WorkshopRegistration
import backend.domain.WorkshopRegistrationState
import backend.user.UserRepository

class RegisteredWorkshops(private val userRepository: UserRepository) {
    fun viewRegisteredWorkshops(userId: Int): List<WorkshopRegistration> {
        val workshops = userRepository.getWorkShopRegistrations(userId)
        return workshops
    }

    // register attendance to a workshop
    fun registerAttendance(
        user: User,
        workshop: Workshop,
    ): WorkshopRegistration {
        // Any logic for making sure a user can register for a workshop
        // Check which reservations this user has
        val registrationsForThisUser = userRepository.getWorkShopRegistrations(user.id)
        if (registrationsForThisUser.map { it.workshop }.contains(workshop)) {
            throw Exception("User already registered for this workshop")
        }
        val workshopRegistrationId = userRepository.addWorkshopRegistrations(user.id, workshop.id)
        return WorkshopRegistration(
            // TODO generate id
            id = 1,
            user = user,
            workshop = workshop,
            // TODO actually check if there is room and set to WAITLIST if not
            state = WorkshopRegistrationState.APPROVED,
        )
    }

// cancel attendance to a workshop
}
