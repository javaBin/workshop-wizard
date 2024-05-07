package backend.service

import backend.config.defaultClient
import backend.dto.WorkshopDTO
import backend.dto.WorkshopListImportDTO
import backend.repository.*
import com.inventy.plugins.DatabaseFactory.Companion.dbQuery
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.config.*
import org.slf4j.LoggerFactory

class UserService(
    private val config: ApplicationConfig,
    private val workshopService: WorkshopService,
    private val userRepository: UserRepository
) {
    private val log = LoggerFactory.getLogger(UserService::class.java)

    suspend fun readByEmail(email: String): User?{
        return userRepository.readByEmail(email)
    }
}