package backend.route

import backend.repository.AdminRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureAdminRoutes(adminRepository: AdminRepository) {
    routing {
        adminRoutes(adminRepository)
    }
}

fun Routing.adminRoutes(adminRepository: AdminRepository) {
}
