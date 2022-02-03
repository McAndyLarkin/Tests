package ui.helpers

import androidx.compose.ui.Alignment
import kotlin.random.Random

object UIUtils {
    fun getRandomHorizontalAlignment() =
        if(Random(123).nextInt(2) % 2 == 0) Alignment.Start else Alignment.End
}