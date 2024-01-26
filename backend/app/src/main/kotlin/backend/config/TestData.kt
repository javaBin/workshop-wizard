package backend.config

import backend.model.*


class TestData {
    companion object {
        val provider1 = listOf(Provider(1, 1, ProviderType.GOOGLE.id, "1234567890"))

        val user1 = User(1, "John", "Doe", "john.doe@example.com", "", provider1)
        val user2 = User(2, "Jane", "Doe", "jane.doe@example.com","", provider1)
        val user3 = User(3, "John", "Smith", "john.smith@example.com","", provider1)

        val workshop1 = Workshop(1, "Kotlin", "John Doe")
        val workshop2 = Workshop(2, "Ktor", "Jane Doe")
        val workshop3 = Workshop(3, "Kotlin Multiplatform", "John Doe")


        val userMap =
            mutableMapOf(
                1 to user1,
                2 to user2,
                3 to user3,
            )

        val registrationMap =
            mutableMapOf(
                1 to WorkshopRegistration(1, user1, workshop1, WorkshopRegistrationState.APPROVED),
                2 to WorkshopRegistration(2, user1, workshop2, WorkshopRegistrationState.APPROVED),
                3 to WorkshopRegistration(3, user2, workshop1, WorkshopRegistrationState.APPROVED),
                4 to WorkshopRegistration(4, user3, workshop3, WorkshopRegistrationState.APPROVED),
            )

        val workshopMap: MutableMap<Int, Workshop> =
            mutableMapOf(
                1 to workshop1,
                2 to workshop2,
                3 to workshop3,
            )

    }
}

