package ui

import actions.Action
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ui.helpers.ColorsHelper
import ui.helpers.RatiosHelper


fun backToFeed() {
    actionManager.send(
        Action.UI.OPEN_PAGE(PageType.FEED)
    )
}

@Composable
fun CloseButton() {
    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.width(RatiosHelper.getMainContentWidth().dp)) {
        Button(onClick = ::backToFeed, modifier = Modifier.align(Alignment.Top).size(50.dp), colors = ColorsHelper.CLEAN_BUTTON_COLORS) {
            Image(painter = painterResource("img1.png"), "Close")
        }
    }
}