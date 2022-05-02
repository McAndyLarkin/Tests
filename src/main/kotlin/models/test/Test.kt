package models.test

import models.test.questions.Question

data class Test(
    val testId: String,
    val name: String,
    val subtitle: String?,
    val description: String,
    val questions: List<Question>
) {
    override fun equals(other: Any?): Boolean {
        return  other is Test &&
                other.name == name && other.subtitle == subtitle &&
                other.description == description && other.questions == questions
    }

    override fun hashCode(): Int {
        return "$name.$subtitle.$description.$questions".hashCode()
    }
}
