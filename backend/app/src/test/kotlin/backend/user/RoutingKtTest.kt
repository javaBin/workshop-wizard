package backend.user

import backend.createServer
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class RoutingKtTest {
    @Test
    fun testPostUserWorkshopWorkshopid() =
        testApplication {
            application {
                createServer()
            }
            client.post("/user/workshop/1").apply {
                assertEquals(HttpStatusCode.OK, this.status)
                assertEquals("Workshop added", this.bodyAsText())
            }
        }
}
