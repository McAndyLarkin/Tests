import actions.Action
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import repositories.RepositoryCenter
import ui.PageType
import ui.actionManager
import ui.helpers.RatiosHelper


@Composable
fun TestPage(testId: String) {
    RepositoryCenter.testRepository.findTestById(testId)?.let { test ->
        Column {

        }
        Text(test.name)
        Box(
            Modifier.fillMaxHeight()
            .width(RatiosHelper.getMainContentWidth().dp)) {
            Column {
                for (question in test.questions) {
                    Text("Question: ${question.title}")
                    Row {
                        val (selectedOption, onOptionSelected) = remember { mutableStateOf("None") }
                        question.variants.forEach { text ->
                            Row(
                                Modifier.width(300.dp)
                                    .selectable(
                                        selected = (text == selectedOption),
                                        onClick = {
                                            onOptionSelected(text)
                                        }
                                    )
                                    .padding(horizontal = 16.dp)
                            ) {
                                RadioButton(
                                    selected = (text == selectedOption),
                                    onClick = {
                                        onOptionSelected(text)
                                    }
                                )
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.body1.merge(),
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
            Row(modifier = Modifier.align(Alignment.BottomEnd)) {
                Button(onClick = ::backToFeed) {
                    Text("Close")
                }
                Button(onClick = ::backToFeed) {
                    Text("Send Answer")
                }
            }
        }
    }
}

fun backToFeed() {
    actionManager.send(
        Action.OPEN_PAGE(PageType.FEED)
    )
}