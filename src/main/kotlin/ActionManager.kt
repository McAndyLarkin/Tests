import androidx.compose.runtime.MutableState
import ticket.Action;

class ActionManager(private val contentState: MutableState<ScreenState>) {
    fun send(buttonAction: Action) {
        when(buttonAction.action) {
            Action.Action.OPEN_PAGE -> openPage(buttonAction.address)
        }
    }

    private fun openPage(address: String) {
        address.split('/').let {
            when (PageType.valueOf(it[0])) {
                PageType.TEST -> openTestPage(it[1])
                PageType.FEED -> openFeedPage()
            }
        }
    }

    private fun openTestPage(testId: String) {
        contentState.value = ScreenState.TEST(testId)
    }

    private fun openFeedPage() {
        contentState.value = ScreenState.FEED
    }
}
