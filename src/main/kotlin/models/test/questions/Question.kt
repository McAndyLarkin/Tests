package models.test.questions

data class Question(
    val number: Int,
    val title: String,
    val type: Type<*>
) {
    sealed class Type<T> private constructor(val rightAnswer: T?) {
        class BINARY(val positive: String, val negative: String) : Type<Boolean>(null){
            override fun toString(): String {
                return "BINARY(positive=$positive, negative=$negative) : ${super.toString()}"
            }

            override fun equals(other: Any?): Boolean {
                return super.equals(other) &&
                        other is BINARY &&
                        positive == other.positive &&
                        negative == other.negative
            }
        }
        class VARIANTS(val variants: List<String>, val poly: Boolean = false) : Type<List<String>>(null){
            override fun toString(): String {
                return "VARIANTS(variants=$variants, poly=$poly) : ${super.toString()}"
            }

            override fun equals(other: Any?): Boolean {
                return super.equals(other) &&
                        other is VARIANTS &&
                        variants == other.variants &&
                        poly == other.poly
            }
        }
        class ENTERABLE : Type<String>(null){
            override fun toString(): String {
                return "ENTERABLE() : ${super.toString()}"
            }
        }

        override fun equals(other: Any?): Boolean {
            return other is Type<*> && rightAnswer == other.rightAnswer
        }

        override fun toString() = "Type(right=$rightAnswer)"
    }
}