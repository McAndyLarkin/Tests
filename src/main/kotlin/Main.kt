import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import localization.LocalizedStrings
import test.TestRepository
import ticket.Action
import viewmodels.Repositiory
import java.awt.Toolkit

class Main

val repository = Repositiory()

val testRepository = TestRepository()

lateinit var actionManager: ActionManager

@Composable
@Preview
fun App() {
    MaterialTheme {
        CommonContent()
    }
}

fun main(args: Array<String>) = application {
    Window(
        state= WindowState(WindowPlacement.Maximized),
        title = LocalizedStrings.instance.APP_NAME,
        onCloseRequest = ::exitApplication, resizable = false) {
        Toolkit.getDefaultToolkit().screenSize.apply {
            RatiosHelper.updateRatios(
                height = height,
                width = width
            )
        }
        App()
    }
}

@Composable
fun CommonContent() {
    Column{
        HeaderBar()
        Row(Modifier.height(RatiosHelper.getUnderHeaderHeight().dp)) {
            MainContent()
            SideBar()
        }
    }
}

@Composable
fun FeedPage() {
    Box(Modifier.fillMaxHeight()
        .width(RatiosHelper.getMainContentWidth().dp)) {

        Column (Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally){

            repository.content.let { content ->
                for (topic in content) {
                    Ticket(topic.title, topic.subtitle, topic.text,
                        topic.img, UIUtils.getRandomHorizontalAlignment(),
                        topic.action)
                }
            }
        }
    }
}

@Composable
fun TestPage(testId: String) {
    testRepository.findTestById(testId)?.let { test ->
        Text(test.name)
        Box(Modifier.fillMaxHeight()
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
                Button(onClick = {
                        actionManager.send(Action(
                            "", Action.Action.OPEN_PAGE, PageType.FEED.toString()
                        ))
                    }) {
                    Text("Close")
                }
                Button(onClick = {
                        actionManager.send(Action(
                            "", Action.Action.OPEN_PAGE, PageType.FEED.toString()
                        ))
                    }) {
                    Text("Send Answer")
                }
            }
        }
    }
}

@Composable
fun LoginPage() {

}

@Composable
fun MainContent() {
    val contentState: MutableState<ScreenState> = remember {
        mutableStateOf(ScreenState.FEED)
    }

    actionManager = ActionManager(contentState)

    when(val screenState = contentState.value) {
        ScreenState.FEED -> FeedPage()
        is ScreenState.TEST -> TestPage(screenState.testId)
        ScreenState.LOGIN -> LoginPage()
    }
}

@Composable
fun SideBar() {
    Box(Modifier.fillMaxHeight().width(RatiosHelper.getSideBarWidth().dp)) {
        Image(
            painter = painterResource("back.jpeg"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = .2f
        )
        Column {
            Text("123");
        }
    }
}

@Composable
fun HeaderBar() {
    Surface(Modifier
        .fillMaxWidth()
        .height(RatiosHelper.getHeaderHeight().dp)) {
        Image(
            painter = painterResource("pilules.jpg"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(Modifier
            .fillMaxSize()) {
            Row {
                Box (Modifier
                    .width(RatiosHelper.getHeaderLeftPartWidth().dp)
                    .fillMaxHeight()
                    .graphicsLayer {
                        shadowElevation = 1.dp.toPx()
                        shape = GenericShape { size, _ ->
                            moveTo(0f, 0f)
                            lineTo(size.width * .62f, 0f)
                            lineTo(size.width, size.height)
                            lineTo(0f, size.height)
                        }
                        clip = true
                    }.background(color = Color(1f, 1f, 1f, .7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource("logo.png"),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight
                    )
                }
                Box (Modifier
                    .width(RatiosHelper.getHeaderMidPartWidth().dp)
                    .fillMaxHeight()
                )
                Box (Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        shadowElevation = 1.dp.toPx()
                        shape = GenericShape { size, _ ->
                            moveTo(0f, 0f)
                            lineTo(size.width , 0f)
                            lineTo(size.width, size.height)
                            lineTo(size.width * .26f, size.height)
                            close()
                        }
                        clip = true
                    }.background(color = Color(1f, 1f, 1f, .7f))
                ) {
                    Column {
                        Text("Информация", Modifier.offset(x = 30.dp), fontSize = 36.sp, color = Color(.5f,.7f,.9f,.9f))
                        Text("Другая информация", Modifier.offset(x = 80.dp), fontSize = 28.sp, color = Color(.5f,.7f,.9f,.85f))
                        Text("Еще информация", Modifier.offset(x = 130.dp), fontSize = 22.sp, color = Color(.5f,.7f,.9f,.8f))
                    }
                }
            }
        }

    }
}

@Composable
fun Ticket(
    title: String,
    subtitle: String?,
    contentText: String,
    contentImg: String?,
    imgAlign: Alignment.Horizontal,
    buttonAction: Action?
) {
    @Composable
    fun Txt() {
        Text(
            text = contentText,
            style = typography.h6.copy(color = Color(.54F, .7F, .7F, 1F)),
            textAlign = TextAlign.Start,
            modifier = Modifier.width(600.dp)
        )
    }

    @Composable
    fun Img() {
        contentImg?.let {
            Image(
                painter = painterResource(contentImg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
        }
    }
    Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .width(900.dp)
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
            text = title,
            style = typography.h4.copy(color = Color(.54F, .65F, .75F, 1F)),
            textAlign = TextAlign.Start,
        )
        subtitle?.let {
            Text(
                text = subtitle,
                style = typography.h5.copy(color = Color(.54F, .65F, .65F, 1F)),
                textAlign = TextAlign.Start,
            )
        }
        Spacer(Modifier.height(20.dp))
        Row {
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
        buttonAction?.let {
            Button(onClick = { actionManager.send(buttonAction) },
                content = { Text(buttonAction.title) }
            )
        }
    }
}

