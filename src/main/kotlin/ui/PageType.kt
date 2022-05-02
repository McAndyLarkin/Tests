package ui

sealed class PageType {
    class TEST(val id: String) : PageType()
    class STATISTIC(val testId: String) : PageType()
    object FEED : PageType()
    object INFO : PageType()
    object LOGIN : PageType()
    object ADMIN : PageType()
    object ANSWERS : PageType()
}