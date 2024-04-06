package backend.repository

import backend.config.TestData
import backend.dto.UserDTO
import com.inventy.plugins.DatabaseFactory.Companion.dbQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class User(
    override val id: Int?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val imageUrl: String,
    val isAdmin: Boolean,
    var providers: List<Provider>
) : Model {

    constructor(
        id: Int,
        firstName: String,
        lastName: String,
        email: String,
        imageUrl: String,
        isAdmin: Boolean
    ) : this(id, firstName, lastName, email, imageUrl, isAdmin, emptyList())
    fun toDTO(): UserDTO {
        return UserDTO(
            firstName,
            lastName,
            email,
            imageUrl,
            isAdmin,
            providers.map { it.toDTO() },
        )
    }
}

class UserRepository{
    val userMap = TestData.userMap

    internal object UserTable : IntIdTable() {
        val firstName = varchar("first_name", 256)
        val lastName = varchar("last_name", 256)
        val email = varchar("email", 256)
        val imageUrl = varchar("image_url", 256)
        val isAdmin = bool("is_admin")

        fun toModel(it: ResultRow) = User(
            it[id].value,
            it[firstName],
            it[lastName],
            it[email],
            it[imageUrl],
            it[isAdmin]
        )

        fun toModel(it: ResultRow, providers: List<Provider>) = User(
            it[id].value,
            it[firstName],
            it[lastName],
            it[email],
            it[imageUrl],
            it[isAdmin],
            providers,
        )

    }

    suspend fun create(user: UserDTO): Int = dbQuery {
        UserTable.insertAndGetId {
            it[firstName] = user.firstName
            it[lastName] = user.lastName
            it[email] = user.email
            it[imageUrl] = user.imageUrl
            it[isAdmin] = user.isAdmin
        }.value
    }

    suspend fun list(): List<User> = dbQuery {
        UserTable.selectAll()
            .map(UserTable::toModel)
    }

    suspend fun readByEmail(email: String): User? {
        return dbQuery {
            UserTable.select { UserTable.email eq email }
                .map(UserTable::toModel)
                .firstOrNull()
        }
    }
}
