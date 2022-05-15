package presenters

import data.delivering.OwnServerService
import models.test.Test
import models.test.answers.AnswersSet

class AnswersPresenter(val dataDeliveringService: OwnServerService) {

    fun findAnswerById(testId: String): Test? = null
    fun findAnswersByTestId(testId: String): List<AnswersSet> =
        getAnswers().filter{ansSet -> ansSet.testId == testId}

    fun add(answers: AnswersSet) {
        dataDeliveringService.addAnswer(answers)
        println("answer added: $answers")
    }

    fun getAnswers() = dataDeliveringService.answers
}