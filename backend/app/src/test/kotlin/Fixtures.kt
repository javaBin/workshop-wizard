import backend.domain.User
import backend.domain.Workshop
import backend.domain.WorkshopRegistration
import backend.domain.WorkshopRegistrationState

object Fixtures {
    val WORKSHOP_1_KOTLIN_WITH_KARI =
        Workshop(id = 1, title = "Kotlin Workshop for new developers", teacherName = "Kari Nordmann")
    val WORKSHOP_2_KOTLIN_WITH_JANE =
        Workshop(
            id = 2,
            title = "Kotlin Workshop for experienced developers",
            teacherName = "Jane Doe",
        )
    val USER_1_OLA_NORDMANN =
        User(id = 1, firstName = "Ola", lastName = "Nordmann", email = "ola.nordmann@example.net")
    val WORKSHOP_REGISTRATION_1_USER_1_TO_WORKSHOP_1_APPROVED =
        WorkshopRegistration(
            id = 1,
            workshop = WORKSHOP_1_KOTLIN_WITH_KARI,
            user = USER_1_OLA_NORDMANN,
            state = WorkshopRegistrationState.APPROVED,
        )
    val WORKSHOP_REGISTRATION_2_USER_1_TO_WORKSHOP_2_WAITLIST =
        WorkshopRegistration(
            id = 2,
            workshop = WORKSHOP_2_KOTLIN_WITH_JANE,
            user = USER_1_OLA_NORDMANN,
            state = WorkshopRegistrationState.WAITLIST,
        )
}
