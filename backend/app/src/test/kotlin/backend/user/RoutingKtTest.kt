package backend.user

import backend.createServer
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*
import kotlin.test.Test

class RoutingKtTest {
    @Test
    fun `calling registerWorkshop is OK when there are no reservations`() =
        testApplication {
            application {
                createServer()
            }
            // mock an authenticated user

            client.post(
                "/user/workshop/2",
            ) {
                // create a basic auth header
                val authHeader = "Basic " + Base64.getEncoder().encodeToString("${"user2"}:${"password"}".toByteArray())
                // set the header
                header(HttpHeaders.Authorization, authHeader)
            }.apply {
                assertEquals(HttpStatusCode.OK, this.status)
                assertEquals("Workshop added", this.bodyAsText())
            }
        }

    @Test
    fun `calling registerWorkshop when the workshop is already registered returns 409 Conflict`() =
        testApplication {
            application {
                createServer()
            }
            // mock an authenticated user

            client.post(
                "/user/workshop/1",
            ) {
                // create a basic auth header
                val authHeader = "Basic " + Base64.getEncoder().encodeToString("${"user"}:${"password"}".toByteArray())
                // set the header
                header(HttpHeaders.Authorization, authHeader)
            }.apply {
                // Relies on the user already being registered for workshop 1 at application startup (see UserRepository)
                assertEquals(HttpStatusCode.Conflict, this.status)
                assertEquals("User already registered for this workshop", this.bodyAsText())
            }
        }
}
