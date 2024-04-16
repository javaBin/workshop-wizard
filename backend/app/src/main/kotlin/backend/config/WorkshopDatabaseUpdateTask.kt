package backend.config

import backend.dto.WorkshopListImportDTO
import backend.repository.Speaker
import backend.repository.SpeakerRepository
import backend.repository.WorkshopRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import com.inventy.plugins.DatabaseFactory.Companion.dbQuery

suspend fun Application.workshopDatabaseUpdate() {

    val workshopRepository = WorkshopRepository()
    val speakerRepository = SpeakerRepository()

    val client = HttpClient {
        defaultClient()
    }

    val response: WorkshopListImportDTO =

        client.get { url(environment.config.property("workshopDatabase.workshopDataUrl").getString()) }.body()

    log.info(response.toString())

    val speakers = extractSpeakers(response)

    dbQuery {
        workshopRepository.update(response.sessions)
        speakerRepository.replace(speakers)
    }

}

private fun extractSpeakers(response: WorkshopListImportDTO): List<Speaker> {
    return response.sessions.flatMap { workshop ->
        workshop.speakers.withIndex().map { (index, speaker) ->
            Speaker(
                name = speaker.name,
                twitter = speaker.twitter ?: "",
                bio = speaker.bio,
                workshopId = workshop.id,
                index = index,
            )
        }
    }
}

fun Application.startWorkshopDatabaseUpdateTask() {
    val intervalMinutes =
        environment.config.property("workshopDatabase.workshopDataUpdateIntervalMinutes").getString().toLong()
    val intervalMillis = intervalMinutes * 60 * 1000

    launch {
        while (isActive) { // isActive is true while the application is running
            log.info("Start task [workshop database update]")
            try {
                workshopDatabaseUpdate()
            } catch (e: Exception) {
                log.info("caught exc :)")
            }

            log.info("Finished task [workshop database update]")
            delay(intervalMillis)
        }
    }
}