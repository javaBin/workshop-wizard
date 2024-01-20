package backend

import Fixtures
import backend.domain.User
import backend.domain.Workshop
import backend.domain.WorkshopRegistration
import backend.domain.WorkshopRegistrationState
import backend.user.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ViewRegisteredWorkshopsTest {
    @Test
    fun `When there is nothing registered, we get an empty list of registered workshops`() {
        val workshopRegistrationMap = mutableMapOf<Int, WorkshopRegistration>()
        val userMap = mutableMapOf<Int, User>()
        val workshopMap = mutableMapOf<Int, Workshop>()
        val userRepository =
            UserRepository(userMap = userMap, registrationMap = workshopRegistrationMap, workshopMap = workshopMap)
        val viewRegisteredWorkshops = ViewRegisteredWorkshops(userRepository)

        val workshops = viewRegisteredWorkshops.viewRegisteredWorkshops(1)

        assertEquals(0, workshops.size)
    }

    @Test
    fun `When we have registered, we get a list of our registered workshops`() {
        val workshopRegistrationMap =
            mutableMapOf(
                1 to Fixtures.WORKSHOP_REGISTRATION_1_USER_1_TO_WORKSHOP_1_APPROVED,
            )
        val userMap =
            mutableMapOf(
                1 to Fixtures.USER_1_OLA_NORDMANN,
            )
        val workshopMap =
            mutableMapOf(
                1 to Fixtures.WORKSHOP_1_KOTLIN_WITH_KARI,
            )
        val userRepository =
            UserRepository(userMap = userMap, registrationMap = workshopRegistrationMap, workshopMap = workshopMap)
        val viewRegisteredWorkshops = ViewRegisteredWorkshops(userRepository)

        val workshops = viewRegisteredWorkshops.viewRegisteredWorkshops(1)

        assertEquals(1, workshops.size)
        assertEquals(WorkshopRegistrationState.APPROVED, workshops[0].state)
    }

    @Test
    fun `When we have registered, we get a list of our registered workshops, even if we have registered for multiple`() {
        val expectedWorkshopRegistration1 = Fixtures.WORKSHOP_REGISTRATION_1_USER_1_TO_WORKSHOP_1_APPROVED
        val expectedWorkshopRegistration2 = Fixtures.WORKSHOP_REGISTRATION_2_USER_1_TO_WORKSHOP_2_WAITLIST
        val workshopRegistrationMap =
            mutableMapOf(
                1 to expectedWorkshopRegistration1,
                2 to expectedWorkshopRegistration2,
            )
        val userMap =
            mutableMapOf(
                1 to Fixtures.USER_1_OLA_NORDMANN,
            )
        val workshopMap =
            mutableMapOf(
                1 to Fixtures.WORKSHOP_1_KOTLIN_WITH_KARI,
                2 to Fixtures.WORKSHOP_2_KOTLIN_WITH_JANE,
            )
        val userRepository =
            UserRepository(userMap = userMap, registrationMap = workshopRegistrationMap, workshopMap = workshopMap)
        val viewRegisteredWorkshops = ViewRegisteredWorkshops(userRepository)

        val workshops = viewRegisteredWorkshops.viewRegisteredWorkshops(1)

        assertEquals(2, workshops.size)
        val actual1 = workshops.first { it.id == expectedWorkshopRegistration1.id }
        val actual2 = workshops.first { it.id == expectedWorkshopRegistration2.id }
        assertEquals(
            WorkshopRegistrationState.APPROVED,
            actual1.state,
        )
        assertEquals(WorkshopRegistrationState.WAITLIST, actual2.state)
    }
}
