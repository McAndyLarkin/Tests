package data.delivering

import models.test.Test
import models.test.answers.Answer
import models.test.answers.AnswersSet

interface DataDeliveringService {
    fun addTest(test: Test)
    val tests: List<Test>

    fun addAnswer(answers: AnswersSet)

    fun login(login: String, password: String): Boolean

    val answers: List<AnswersSet>
}