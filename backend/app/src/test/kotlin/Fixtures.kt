import backend.model.User
import backend.model.Workshop
import backend.model.WorkshopRegistration
import backend.model.WorkshopRegistrationState

object Fixtures {
    val WORKSHOP_1_KOTLIN_WITH_KARI =
        Workshop(id = 1, title = "Kotlin Workshop for new developers", teacherName = "Kari Nordmann")
    val USER_1_OLA_NORDMANN =
        User(id = 1, firstName = "Ola", lastName = "Nordmann", email = "ola.nordmann@example.net", imageUrl = "", providers = listOf())
    val WORKSHOP_REGISTRATION_1_USER_1_TO_WORKSHOP_1_APPROVED =
        WorkshopRegistration(
            id = 1,
            workshop = WORKSHOP_1_KOTLIN_WITH_KARI,
            user = USER_1_OLA_NORDMANN,
            state = WorkshopRegistrationState.APPROVED,
        )
}
