package viewmodels

import ticket.Action
import ticket.TicketTopic

class Repositiory {
    var content: List<TicketTopic> = listOf(
        TicketTopic("Title1", "Subtitle", "back.jpeg", "Lorem Ipsum",
            Action("Button", Action.Action.OPEN_PAGE,"TEST/1")
        )
    )
    private set
}