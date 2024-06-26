package backend.config

import backend.repository.AdminRepository
import backend.repository.SpeakerRepository
import backend.repository.UserRepository
import backend.repository.WorkshopRepository
import backend.route.*
import backend.service.UserService
import backend.service.WorkshopService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val userRepository = UserRepository()
    val adminRepository = AdminRepository()
    val workshopRepository = WorkshopRepository()
    val speakerRepository = SpeakerRepository()
    val workshopService = WorkshopService(environment.config, workshopRepository, speakerRepository)
    val userService = UserService(environment.config, workshopService, userRepository)

    configureAuth0Route(userRepository)
    configureUserRoutes(userService)
    configureWorkshopRoutes(workshopService)
    configureAdminRoutes(adminRepository)
    configureApiRoutes()

    routing {
        authenticate("auth0-user") {
            get("/auth") {
                call.respondText("Hello World!")
            }
        }

        get {
            call.respondText("Hello World!")
        }
    }
}
