package backend.model

class Provider(
    override val id: Int,
    val userId: Int,
    val providerType: Int,
    val providerId: String,
) : Model {
}
