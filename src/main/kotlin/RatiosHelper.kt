import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Shape

class RatiosHelper private constructor(
    private val window: Size
){

    companion object {
        private lateinit var ratiosHelper: RatiosHelper

        fun updateRatios(height: Int, width: Int) {
            ratiosHelper = RatiosHelper(
                Size(
                    height = height,
                    width = width
                )
            )
        }

        fun getHeaderHeight(): Int = ratiosHelper.window.height.div(9)

        fun getHeaderLeftPartWidth(): Int = ratiosHelper.window.width.div(6)

        fun getHeaderMidPartWidth(): Int = ratiosHelper.window.width
            .minus(getHeaderLeftPartWidth())
            .minus(getHeaderRightPartWidth())

        fun getHeaderRightPartWidth(): Int = ratiosHelper.window.width.div(4)

        fun getWindowHeight(): Int = ratiosHelper.window.height

        fun getWindowWidth(): Int = ratiosHelper.window.width

        fun getUnderHeaderHeight(): Int = ratiosHelper.window.height - getHeaderHeight()

        fun getSideBarWidth(): Int = ratiosHelper.window.width.div(5)

        fun getMainContentWidth(): Int = ratiosHelper.window.width - ratiosHelper.window.width.div(5)
    }

    private class Size(
        val height: Int,
        val width: Int
    )
}