package models.test.answers

data class AnswersSet(
    val testId: String,
    val id: Long?,
    val author: String?,
    val answers: List<Answer<*>>
) {
    val count: Int = answers.size
}
