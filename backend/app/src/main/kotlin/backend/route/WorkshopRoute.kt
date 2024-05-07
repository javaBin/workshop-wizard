package backend.route

import backend.service.WorkshopService
import backend.repository.SpeakerRepository
import backend.repository.WorkshopRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureWorkshopRoutes(workshopRepository: WorkshopRepository,
                                        speakerRepository: SpeakerRepository,
                                        workshopService: WorkshopService
) {
    routing {
        workshopRoute(workshopRepository, speakerRepository, workshopService)
    }
}

fun Routing.workshopRoute(workshopRepository: WorkshopRepository,
                          speakerRepository: SpeakerRepository,
                          workshopService: WorkshopService
) {
    authenticate("auth0-user") {
        get("/workshop") {
            val speakers = speakerRepository.list()
                .groupBy ({ it.workshopId }, {it.toDTO()} )

            val workshops = workshopRepository.list().map { it.toDTO(speakers[it.id])  }

            call.respond(workshops)
        }
    }
    authenticate("auth0-admin") {
        post("/update-workshop") {
            workshopService.workshopDatabaseUpdate()
            call.respond(HttpStatusCode.OK)
        }
    }
}

