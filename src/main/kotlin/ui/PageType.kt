package ui

sealed class PageType {
    class TEST(val id: String) : PageType()
    object FEED : PageType()
    object INFO : PageType()
    object LOGIN : PageType()
}