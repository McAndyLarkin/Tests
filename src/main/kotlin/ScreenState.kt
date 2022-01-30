sealed class ScreenState {
    object FEED : ScreenState()
    class TEST(val testId: String) : ScreenState()
    object LOGIN : ScreenState()
}