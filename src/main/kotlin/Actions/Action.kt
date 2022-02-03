package Actions

import PageType

sealed class Action{
    class OPEN_PAGE(val page: PageType) : Action()
    class OPEN_WEB(val address: String) : Action()
}