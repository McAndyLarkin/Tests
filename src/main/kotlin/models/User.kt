package models

open class User(
    val userId: String
) {
    companion object {
        object ANONYMOUS : User("anonymous")
        object ADMIN : User("admin")
    }
}
