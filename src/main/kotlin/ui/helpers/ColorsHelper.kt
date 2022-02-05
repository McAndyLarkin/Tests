package ui.helpers

import androidx.compose.material.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

object ColorsHelper {
    val HEADER_LINES = Color(.5f,.7f,.9f,.9f)
    val HEADER_LINES_FOCUS = Color(.3f,.3f,1f,1f)

    val PASS_TEST_BUTTON = Color(66, 158, 157, 0xFA)

    val CLEAN_BUTTON_COLORS = object : ButtonColors {
        @Composable
        override fun backgroundColor(enabled: Boolean): State<Color> {
            return rememberUpdatedState(Color.Transparent)
        }

        @Composable
        override fun contentColor(enabled: Boolean): State<Color> {
            return rememberUpdatedState(PASS_TEST_BUTTON)
        }

    }
}