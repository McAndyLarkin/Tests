package repositories

import Actions.Action
import models.Entity

class AppRepository {
    val headerContent: List<Entity> by lazy { listOf(
        Entity("Информация", Action.OPEN_PAGE(PageType.INFO)),
        Entity("Сайт", Action.OPEN_WEB("https://www.google.com/?client=safari")),
        Entity("Тел: +7 888 999 66 55")
    )}
}