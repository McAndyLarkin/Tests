package actions

import models.test.Test
import ui.PageType

sealed class Action{
    sealed class UI : Action() {
        class OPEN_PAGE(val page: PageType) : UI()
        class OPEN_WEB(val address: String) : UI()
    }
    sealed class INTERNAL : Action() {
        class ADD_TEST(val test: Test) : INTERNAL()
        class ON_NEW_TEST(val testPath: String) : INTERNAL()
    }
}