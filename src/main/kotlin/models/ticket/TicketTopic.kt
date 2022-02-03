package models.ticket

import models.Entity

data class TicketTopic(
    val title: String,
    val subtitle: String?,
    val img: String?,
    val text: String,
    val button: Entity?
)
