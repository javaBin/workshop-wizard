package backend

import backend.admin.AdminRepository
import backend.admin.adminRoutes
import backend.user.UserRepository
import backend.user.userRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun server() =
    embeddedServer(factory = Netty, port = 8080) {
        createServer()
    }

fun Application.createServer() {
    runMigration()
    configureAuth()
    configureRouting()
}

class CustomPrincipal(val userId: Int) : Principal

fun Application.configureAuth() {
    authentication {
        basic(name = "basic") {
            realm = "Ktor Server"
            validate { credentials ->
                if (credentials.name == "user" && credentials.password == "password") {
                    CustomPrincipal(1)
                } else {
                    null
                }
            }
        }
    }
}

fun Application.configureRouting() {
    val userRepository = UserRepository()
    val adminRepository = AdminRepository()

    install(ContentNegotiation) {
        json()
    }
    routing {
        userRoutes(userRepository)
        adminRoutes(adminRepository)
        healthz()

        get {
            call.respondText("Hello, world!")
        }
    }
}

private fun Routing.healthz() {
    get("readiness") {
        call.respondText("READY!")
    }

    get("liveness") {
        call.respondText("ALIVE!")
    }
}

fun main() {
    server().start(wait = true)
}
