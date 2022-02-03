package actions

import ui.PageType
import androidx.compose.runtime.MutableState

class ActionManager(private val contentState: MutableState<PageType>) {
    fun send(action: Action) {
        when(action) {
            is Action.OPEN_PAGE -> openPage(action.page)
            is Action.OPEN_WEB -> openWeb(action.address)
        }
    }

    private fun openPage(page: PageType) {
        when (page) {
            is PageType.TEST -> openTestPage(page.id)
            PageType.FEED -> openFeedPage()
        }
    }

    private fun openTestPage(testId: String) {
        contentState.value = PageType.TEST(testId)
    }

    private fun openFeedPage() {
        contentState.value = PageType.FEED
    }

    private fun openWeb(link: String) {
        println("Open web: $link")
    }
}
