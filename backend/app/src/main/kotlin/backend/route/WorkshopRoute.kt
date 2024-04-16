package backend.route

import backend.repository.Speaker
import backend.repository.SpeakerRepository
import backend.repository.Workshop
import backend.repository.WorkshopRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureWorkshopRoutes(workshopRepository: WorkshopRepository, speakerRepository: SpeakerRepository) {
    routing {
        workshopRoute(workshopRepository, speakerRepository)
    }
}

fun Routing.workshopRoute(workshopRepository: WorkshopRepository, speakerRepository: SpeakerRepository) {
    authenticate("auth0-user") {
        get("/workshop") {
            val speakers = speakerRepository.list()
                .groupBy ({ it.workshopId }, {it.toDTO()} )

            val workshops = workshopRepository.list().map { it.toDTO(speakers[it.id])  }

            call.respond(workshops)
        }
    }

}

