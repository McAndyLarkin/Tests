package repositories

import actions.Action
import models.Entity
import ui.PageType

class AppRepository {
    val headerContent: List<Entity> by lazy { listOf(
        Entity("Информация", Action.UI.OPEN_PAGE(PageType.INFO)),
        Entity("Сайт", Action.UI.OPEN_WEB("https://www.nntu.ru")),
        Entity("Тел: +7 888 999 66 55")
    )}
}