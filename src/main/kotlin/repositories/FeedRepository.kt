package repositories

import Actions.Action
import models.Entity
import models.ticket.TicketTopic

class FeedRepository {
    var feed: List<TicketTopic> = listOf(
        TicketTopic("Title1", "Subtitle", "back.jpeg", "Lorem Ipsum",
            Entity("Button", Action.OPEN_PAGE(PageType.TEST("1")))
        )
    )
    private set
}