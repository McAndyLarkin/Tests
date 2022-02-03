package models.test

import models.test.questions.Question

data class Test(
    val testId: String,
    val name: String,
    val questions: List<Question>
)
