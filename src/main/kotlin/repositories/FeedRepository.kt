package repositories

import ui.viewmodels.ticket.TicketTopic

class FeedRepository {
    var feed: List<TicketTopic> = listOf(
        TicketTopic("Новость", null, "test1.png", "Текст", null)
    )
    private set
}