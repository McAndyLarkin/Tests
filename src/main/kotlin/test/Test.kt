package test

import test.questions.Question

data class Test(
    val testId: String,
    val name: String,
    val questions: List<Question>
)
