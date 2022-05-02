package actions

import ui.PageType
import androidx.compose.runtime.MutableState
import data.delivering.FilesResolver
import repositories.SingletonCenter

class ActionManager(private val contentState: MutableState<PageType>) {
    fun send(action: Action) {
        when(action) {
            is Action.UI -> processUIAction(action)
            is Action.INTERNAL -> processInternalAction(action)
        }
    }

    private fun processUIAction(action: Action.UI) {
        when(action) {
            is Action.UI.OPEN_PAGE -> openPage(action.page)
            is Action.UI.OPEN_WEB -> openWeb(action.address)
        }
    }

    private fun processInternalAction(action: Action.INTERNAL) {
        when(action) {
            is Action.INTERNAL.ON_NEW_TEST -> onNewTest(action.testPath)
            is Action.INTERNAL.ADD_TEST -> SingletonCenter.testRepository.add(action.test)
            is Action.INTERNAL.ADD_ANSWER -> SingletonCenter.answerRepository.add(action.answers)
            is Action.INTERNAL.LOG_IN -> SingletonCenter.authRepository.logIn(action.login, action.password)
        }
    }

    private fun openPage(page: PageType) {
        contentState.value = page
    }

    private fun openWeb(link: String) {
        println("Open web: $link")
    }

    private fun onNewTest(path: String) {
        FilesResolver.getTestFrom(path) { ex , title -> println("$title : ${ex.message}") }
            ?.let{test -> this.send(Action.INTERNAL.ADD_TEST(test)) }
    }
}
