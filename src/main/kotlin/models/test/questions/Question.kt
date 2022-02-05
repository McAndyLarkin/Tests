package models.test.questions

data class Question(
    val number: Int,
    val title: String,
    val type: Type<*>
) {
    sealed class Type<T> {
        class BINARY(val positive: String, val negative: String) : Type<Boolean>()
        class VARIANTS(val variants: List<String>, val poly: Boolean = false) : Type<List<String>>()
        class ENTERABLE : Type<String>()
    }
}