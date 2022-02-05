package ui

import actions.Action
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.helpers.ColorsHelper
import ui.helpers.RatiosHelper
import java.awt.FileDialog

@Composable
fun AdminPage(window: ComposeWindow) {
    Column(modifier = Modifier.width(RatiosHelper.getMainContentWidth().dp).padding(20.dp)) {
        Row(Modifier.fillMaxWidth()) {
            Header()
            CloseButton()
        }
        Button(onClick = { addTestDialog(window) } ) {
            Text("Add Tests")
        }
    }
}

@Composable
private fun Header() {
    Column(Modifier.padding(bottom = 20.dp)) {
        Text("Admin panel", fontSize = 28.sp, color = ColorsHelper.PASS_TEST_BUTTON)
    }
}

private fun addTestDialog(window: ComposeWindow) {
    val file = FileDialog(window, "Select new Test", FileDialog.LOAD).apply {
        isVisible = true
        isMultipleMode = false
//        val fileExt = "*.json"

//        if (getSystem() == OS.Windows) {
//            file = fileExt
//        }
    }.files.firstOrNull()

    file?.absolutePath?.let { path ->
        actionManager.send(Action.INTERNAL.ON_NEW_TEST(path))
    }
}
