package data.delivering

import models.test.Test
import models.test.questions.Answer

interface DataDeliveringService {
    fun addTest(test: Test)
    val tests: List<Test>

    fun addAnswer(answer: Answer<*>)
    val answers: List<Answer<*>>
}