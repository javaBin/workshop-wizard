package backend

import backend.config.configureAuth
import backend.route.adminRoutes
import backend.route.userRoutes
import backend.repository.AdminRepository
import backend.repository.UserRepository
import backend.repository.WorkshopRepository
import backend.route.workshopRoute
import com.inventy.plugins.DatabaseFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    DatabaseFactory(
        dbHost = environment.config.property("database.host").getString(),
        dbPort = environment.config.property("database.port").getString(),
        dbUser = environment.config.property("database.user").getString(),
        dbPassword = environment.config.property("database.password").getString(),
        databaseName = environment.config.property("database.databaseName").getString(),
        embedded = environment.config.property("database.embedded").getString().toBoolean(),
    ).init()
    configureAuth()
    configureRouting()
}


fun Application.configureRouting() {
    val userRepository = UserRepository()
    val adminRepository = AdminRepository()
    val workshopRepository = WorkshopRepository()

    install(ContentNegotiation) {
        json()
    }
    routing {
        userRoutes(userRepository)
        adminRoutes(adminRepository, userRepository)
        workshopRoute(workshopRepository)
        healthz()

        authenticate ("auth0-user") {
            get("/auth") {
                call.respondText("Hello, world!")
            }
        }

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

