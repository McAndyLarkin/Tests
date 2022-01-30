package ticket

data class TicketTopic(
    val title: String,
    val subtitle: String?,
    val img: String?,
    val text: String,
    val action: Action?
)

data class Action(
    val title: String,
    val action: Action,
    val address: String
) {

    enum class Action{
        OPEN_PAGE
    }
}
