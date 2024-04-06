package backend.route

import backend.dto.UserDTO
import backend.repository.ProviderRepository
import backend.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureAuth0Route(userRepository: UserRepository, providerRepository: ProviderRepository) {
    routing {
        auth0Route(userRepository, providerRepository)
    }
}

fun Route.auth0Route(userRepository: UserRepository, providerRepository: ProviderRepository) {
    authenticate ("basic-auth0") {
        post("/auth0") {
            val userDTO = call.receive<UserDTO>()
            val user = userRepository.readByEmail(userDTO.email)
            if (user?.id != null) {
                providerRepository.updateProvidersForUser(user.id, userDTO.providers)
                call.respond(HttpStatusCode.OK)
                return@post
            } else {
                val create = userRepository.create(userDTO)
                application.log.info("User created with id: $create")
                call.respond(HttpStatusCode.Created)
                return@post
            }
        }
    }
}
