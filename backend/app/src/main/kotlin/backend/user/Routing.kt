package backend.user

import backend.CustomPrincipal
import backend.RegisteredWorkshops
import backend.domain.User
import backend.domain.Workshop
import backend.domain.WorkshopRegistration
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.userRoutes(userRepository: UserRepository) {
    authenticate("basic") {
        get("/user/workshop") {
            // Should be based on the logged in user
            val userId = call.authentication.principal<CustomPrincipal>()?.userId!!
            val workshopRegistrations = RegisteredWorkshops(userRepository).viewRegisteredWorkshops(userId)
            val workshopRegistrationsDTO = workshopRegistrations.map { WorkshopRegistrationDTO.fromDomain(it) }
            call.respond(workshopRegistrationsDTO)
        }

        post("/user/workshop/{workshopId}") {
            try {
                val userId = call.authentication.principal<CustomPrincipal>()?.userId!!
                val workshopId = call.parameters["workshopId"]!!.toInt()
                val workshopRegistration =
                    RegisteredWorkshops(userRepository).registerAttendance(
                        user = userRepository.userMap[userId]!!,
                        workshop = userRepository.workshopMap[workshopId]!!,
                    )
                call.respondText("Workshop added")
            } catch (e: Exception) {
                call.respondText(
                    "Could not add workshop for some reason",
                    status = io.ktor.http.HttpStatusCode.InternalServerError,
                )
            }
        }

        put("/user/workshop/{workshopId}/cancel") {
            val userId = call.authentication.principal<CustomPrincipal>()?.userId!!
            userRepository.cancelWorkshopRegistration(userId, call.parameters["workshopId"]!!.toInt())
            call.respondText("Workshop cancelled")
        }
    }
}

private fun WorkshopRegistrationDTO.Companion.fromDomain(workshop: WorkshopRegistration): WorkshopRegistrationDTO {
    return WorkshopRegistrationDTO(
        id = workshop.id,
        user = UserDTO.fromDomain(workshop.user),
        workshop = WorkshopDTO.fromDomain(workshop.workshop),
        state = workshop.state.toString(),
    )
}

private fun WorkshopDTO.Companion.fromDomain(workshop: Workshop): WorkshopDTO {
    return WorkshopDTO(
        title = workshop.title,
        teacherName = workshop.teacherName,
    )
}

private fun UserDTO.Companion.fromDomain(user: User): UserDTO {
    return UserDTO(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
    )
}
