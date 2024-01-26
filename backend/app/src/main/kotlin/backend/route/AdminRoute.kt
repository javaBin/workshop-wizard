package backend.route

import backend.repository.AdminRepository
import backend.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.adminRoutes(adminRepository: AdminRepository, userRepository: UserRepository) {
    get("/admin/workshop") {
        call.respond(adminRepository.getWorkshops())
    }
    get("/admin/workshop/{workshopId}") {
        try {
            call.respond(adminRepository.getWorkshopRegistrations(call.parameters["workshopId"]!!.toInt()))
        } catch (e: Exception) {
            call.respondText("Workshop not found", status = io.ktor.http.HttpStatusCode.NotFound)
        }
    }
    get("/admin/user") {
        call.respond(userRepository.getUsers())
    }
}
