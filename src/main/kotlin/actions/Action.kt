package actions

import models.test.Test
import models.test.answers.Answer
import models.test.answers.AnswersSet
import ui.PageType

sealed class Action{
    sealed class UI : Action() {
        class OPEN_PAGE(val page: PageType) : UI()
        class OPEN_WEB(val address: String) : UI()
    }
    sealed class INTERNAL : Action() {
        class ADD_TEST(val test: Test) : INTERNAL()
        class ADD_ANSWER(val answers: AnswersSet) : INTERNAL()
        class ON_NEW_TEST(val testPath: String) : INTERNAL()
        class LOG_IN(val login: String, val password: String) : INTERNAL()
    }
}