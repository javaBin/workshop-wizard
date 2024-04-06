package backend.config

import backend.repository.AdminRepository
import backend.repository.ProviderRepository
import backend.repository.UserRepository
import backend.repository.WorkshopRepository
import backend.route.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val userRepository = UserRepository()
    val providerRepository = ProviderRepository()
    val adminRepository = AdminRepository()
    val workshopRepository = WorkshopRepository()

    configureAuth0Route(userRepository, providerRepository)
    configureUserRoutes(userRepository)
    configureWorkshopRoutes(workshopRepository)
    configureAdminRoutes(adminRepository)
    configureApiRoutes()

    routing {
        authenticate ("auth0-user") {
            get("/auth") {
                call.respondText("Hello World!")
            }
        }

        get {
            call.respondText("Hello World!")
        }
    }
}
