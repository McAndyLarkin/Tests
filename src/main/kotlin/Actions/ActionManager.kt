package Actions

import PageType
import ScreenState
import androidx.compose.runtime.MutableState

class ActionManager(private val contentState: MutableState<ScreenState>) {
    fun send(action: Action) {
        when(action) {
            is Action.OPEN_PAGE -> openPage(action.page)
        }
    }

    private fun openPage(page: PageType) {
        when (page) {
            is PageType.TEST -> openTestPage(page.id)
            PageType.FEED -> openFeedPage()
        }
    }

    private fun openTestPage(testId: String) {
        contentState.value = ScreenState.TEST(testId)
    }

    private fun openFeedPage() {
        contentState.value = ScreenState.FEED
    }
}
