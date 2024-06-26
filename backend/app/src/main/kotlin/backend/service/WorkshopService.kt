package backend.service

import backend.config.defaultClient
import backend.dto.WorkshopDTO
import backend.dto.WorkshopListImportDTO
import backend.repository.Speaker
import backend.repository.SpeakerRepository
import backend.repository.WorkshopRepository
import com.inventy.plugins.DatabaseFactory.Companion.dbQuery
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.config.*
import org.slf4j.LoggerFactory

class WorkshopService(
    private val config: ApplicationConfig,
    private val workshopRepository: WorkshopRepository,
    private val speakerRepository: SpeakerRepository
) {
    private val log = LoggerFactory.getLogger(WorkshopService::class.java)


    suspend fun getWorkshops(): List<WorkshopDTO> {
        val speakers = speakerRepository.list()
            .groupBy({ it.workshopId }, { it.toDTO() })

        return workshopRepository.list().map { it.toDTO(speakers[it.id]) }
    }

    suspend fun workshopDatabaseUpdate() {
        val client = HttpClient {
            defaultClient()
        }

        val response: WorkshopListImportDTO =
            client.get { url(config.property("workshopDatabase.workshopDataUrl").getString()) }.body()

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
}