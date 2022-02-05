package ui.viewmodels.ticket

import actions.Action
import models.Entity
import models.test.Test
import ui.PageType

object TicketMapper {
    fun fromTest(test: Test) = TicketTopic(
        test.name, test.subtitle, "test1.png", test.description,
        Entity("Пройти тест", Action.UI.OPEN_PAGE(PageType.TEST(test.testId)))
    )
}