package ui

import actionManager
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.viewmodels.ticket.TicketTopic
import presenters.SingletonCenter
import ui.helpers.ColorsHelper
import ui.helpers.RatiosHelper
import ui.helpers.UIUtils
import ui.viewmodels.ticket.TicketMapper


@Composable
fun FeedPage() {
    Box(
        Modifier.fillMaxHeight()
        .width(RatiosHelper.getMainContentWidth().dp)) {

        Column (
            Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally){

            SingletonCenter.testingPresenter.getTests().also(::println)
                .map(TicketMapper::fromTest).let { content ->
                for (topic in content) {
                    Ticket(topic)
                }
            }
        }
    }
}

@Composable
private fun Ticket(ticket: TicketTopic) {
    @Composable
    fun Txt() {
        Text(
            text = ticket.text,
            fontSize = 18.sp,
            color = Color(.54F, .7F, .7F, 1F),
            textAlign = TextAlign.Start,
            modifier = Modifier.width(600.dp)
        )
    }

    @Composable
    fun Img() {
        ticket.img?.let {
            Image(
                painter = painterResource(ticket.img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
        }
    }
    Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .width(1000.dp)
        .padding(32.dp)
        .border(width = 4.dp, color = Color.LightGray, CutCornerShape(32.dp))
        .graphicsLayer {
            shadowElevation = 8.dp.toPx()
            shape = CutCornerShape(32.dp)
            clip = true
        }
        .background(color = Color(.95f,1f, 1f, 1f))
        .padding(32.dp)) {

        Text(
            text = ticket.title,

            style = MaterialTheme.typography.h5.copy(color = Color(.54F, .65F, .75F, 1F)),
            textAlign = TextAlign.Start,
        )
        ticket.subtitle?.let {
            Text(
                text = ticket.subtitle,
                style = MaterialTheme.typography.h6.copy(color = Color(.54F, .65F, .65F, 1F)),
                textAlign = TextAlign.Start,
            )
        }
        Spacer(Modifier.height(20.dp))
        Row {
            val imgAlign = UIUtils.getRandomHorizontalAlignment()
            if (imgAlign == Alignment.Start){
                Img()
                Spacer(Modifier.width(20.dp))
                Txt()
            } else if (imgAlign == Alignment.End) {
                Txt()
                Spacer(Modifier.width(20.dp))
                Img()
            }
        }
        Row(modifier = Modifier.align(Alignment.End).fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ticket.button?.let { action ->
                Button(onClick = { action.action?.let(actionManager::send) }, colors = ColorsHelper.CLEAN_BUTTON_COLORS) {
                    Text(action.title)
                }
            }
        }
    }
}

