package backend.model

class WorkshopRegistration(
    val id: Int,
    val user: User,
    val workshop: Workshop,
    var state: WorkshopRegistrationState = WorkshopRegistrationState.PENDING,
)
