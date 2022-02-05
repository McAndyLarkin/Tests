package repositories

import actions.Action
import models.Entity
import models.ticket.TicketTopic
import ui.PageType

class FeedRepository {
    var feed: List<TicketTopic> = listOf(
        TicketTopic("Шкала ситуативной тревожности", null, "test1.png", "Прочитайте внимательно каждое из приведенных ниже\n" +
                "предложений и зачеркните цифру в соответствующей графе справа в\n" +
                "зависимости от того, как вы себя чувствуете в данный момент. Над\n" +
                "вопросами долго не задумывайтесь, поскольку правильных и\n" +
                "неправильных ответов нет.",
            Entity("Пройти тест", Action.OPEN_PAGE(PageType.TEST("1")))
        ),
        TicketTopic("Шкала личной тревожности", null, "test.png", "Прочитайте внимательно каждое из приведенных ниже\n" +
                "предложений и зачеркните цифру в соответствующей графе справа в\n" +
                "зависимости от того, как вы себя чувствуете обычно. Над вопросами\n" +
                "долго не думайте, поскольку правильных или неправильных ответов нет.",
            Entity("Пройти тест", Action.OPEN_PAGE(PageType.TEST("2")))
        ),
    )
    private set
}