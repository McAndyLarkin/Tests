package repositories

import models.test.Test
import models.test.answers.Answer
import models.test.questions.Question

class AnswerHolder private constructor(val answers: MutableList<Answer<*>>) {
    private inline fun <reified A, B>getNewAnswer(answer: Answer<A>, newValue: B) = answer.apply {
        if (newValue is A)
            value = newValue
    }
    companion object {
        fun forTest(test: Test) = ArrayList<Answer<*>>(test.questions.size).apply {
            test.questions.forEach { quest ->
                add(getAnswerForQuestionType(quest.type))
            }
        }.let { AnswerHolder(it) }

        private fun <T> getAnswerForQuestionType(type: Question.Type<T>) = when (type) {
            is Question.Type.BINARY -> Answer.BinaryAnswer()
            is Question.Type.ENTERABLE -> Answer.EnterableAnswer()
            is Question.Type.VARIANTS -> Answer.VariantsAnswer()
            is Question.Type.NUM_ENTERABLE -> Answer.NumEnterableAnswer()
        }
    }
}
