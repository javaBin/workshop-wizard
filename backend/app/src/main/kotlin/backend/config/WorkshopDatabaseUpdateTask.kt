package backend.config

import backend.dto.WorkshopListImportDTO
import backend.repository.WorkshopRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

suspend fun Application.workshopDatabaseUpdate() {

    val workshopRepository = WorkshopRepository()

    val client = HttpClient {
        defaultClient()
    }

    val response: WorkshopListImportDTO =

        client.get { url(environment.config.property("workshopDatabase.workshopDataUrl").getString()) }.body()

    log.info(response.toString())

    workshopRepository.create(response)
}

fun Application.startWorkshopDatabaseUpdateTask() {
    val intervalMinutes = environment.config.property("workshopDatabase.workshopDataUpdateIntervalMinutes").getString().toLong()
    val intervalMillis = intervalMinutes * 60 * 1000

    launch {
        while (isActive) { // isActive is true while the application is running
            log.info("Start task [workshop database update]")
            workshopDatabaseUpdate()
            log.info("Finished task [workshop database update]")
            delay(intervalMillis)
        }
    }
}