package backend.route

import backend.config.CustomPrincipal
import backend.dto.UserDTO
import backend.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureUserRoutes(userRepository: UserRepository) {
    routing {
        userRoutes(userRepository)
    }
}

fun Routing.userRoutes(userRepository: UserRepository) {
    get("/user/workshop") {
        // Should be based on the logged in user
        val userId = call.authentication.principal<CustomPrincipal>()?.userId!!
        call.respond(userRepository.getWorkShopRegistrations(userId))
    }

    post("/user/workshop/{workshopId}") {
        try {
            val userId = call.authentication.principal<CustomPrincipal>()?.userId!!
            userRepository.addWorkshopRegistrations(userId, call.parameters["workshopId"]!!.toInt())
            call.respondText("Workshop added")
        } catch (e: Exception) {
            call.respondText("Workshop not found", status = io.ktor.http.HttpStatusCode.NotFound)
        }
    }

    put("/user/workshop/{workshopId}/cancel") {
        val userId = call.authentication.principal<CustomPrincipal>()?.userId!!
        userRepository.cancelWorkshopRegistration(userId, call.parameters["workshopId"]!!.toInt())
        call.respondText("Workshop cancelled")
    }
    authenticate ("basic-auth0") {
        post("/user") {
            val userDTO = call.receive<UserDTO>()
            val user = userRepository.readByEmail(userDTO.email)
            if (user?.id != null) {
                userRepository.updateProviders(user.id, user.providers, userDTO.providers)
                call.respond(HttpStatusCode.OK)
                return@post
            } else {
                val create = userRepository.create(userDTO)
                call.respond(HttpStatusCode.Created, create)
                return@post
            }
        }
    }
}
