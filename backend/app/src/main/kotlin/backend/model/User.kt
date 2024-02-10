package backend.model

class User(override val id: Int?, val firstName: String, val lastName: String, val email: String, val imageUrl: String,
           var providers: List<Provider>) : Model
