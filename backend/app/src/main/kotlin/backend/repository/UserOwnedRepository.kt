package backend.repository

import backend.config.CustomPrincipal
import io.ktor.server.auth.*

class UserOwnedRepository {
    open class UserOwnedRepository(val userId: Long) {
        companion object {
            inline fun <T, reified R: UserOwnedRepository> userContext(auth: AuthenticationContext, block: (repository: R) -> T): T {
                val principal = auth.principal<CustomPrincipal>()
                val repository = R::class.java.getDeclaredConstructor(Long::class.java).newInstance(principal!!.userId)
                return block(repository)
            }
        }
    }
}
