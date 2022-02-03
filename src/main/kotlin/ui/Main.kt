package ui

import actions.ActionManager
import TestPage
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import localization.LocalizedStrings
import repositories.RepositoryCenter
import ui.helpers.ColorsHelper
import ui.helpers.RatiosHelper
import java.awt.Toolkit

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
fun MainContent() {
    val contentState: MutableState<PageType> = remember {
        mutableStateOf(PageType.FEED)
    }

    actionManager = ActionManager(contentState)

    when(val pageType = contentState.value) {
        PageType.FEED -> FeedPage()
        is PageType.TEST -> TestPage(pageType.id)
        PageType.LOGIN -> LoginPage()
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
            Text("123")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
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
                    Column(Modifier.padding(20.dp)) {
                        RepositoryCenter.appRepository.headerContent.let { lines ->
                            for (index in lines.indices) {
                                RepositoryCenter.appRepository.headerContent
                                val color = remember{mutableStateOf(ColorsHelper.HEADER_LINES)}
                                Text(lines[index].title, Modifier.offset(x = 30.dp + (index * 40).dp).let { mod ->
                                    lines[index].action?.let {
                                        mod.onPointerEvent(
                                            PointerEventType.Enter, PointerEventPass.Main
                                        ) {
                                            color.value = ColorsHelper.HEADER_LINES_FOCUS
                                        }.onPointerEvent(PointerEventType.Exit, PointerEventPass.Main) {
                                            color.value = ColorsHelper.HEADER_LINES
                                        }.onPointerEvent(PointerEventType.Press, PointerEventPass.Main) {
                                            lines[index].action?.let(actionManager::send)
                                        }
                                    } ?: mod
                                }, fontSize = 24.sp, color = color.value)
                            }
                        }
                    }
                }
            }
        }
    }
}
