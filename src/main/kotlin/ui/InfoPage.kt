package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.helpers.RatiosHelper

@Composable
fun InfoPage() {
    Column(modifier = Modifier
        .width(RatiosHelper.getMainContentWidth().dp)
        .padding(20.dp)
        .verticalScroll(rememberScrollState())) {
        Row(Modifier.fillMaxWidth()) {
            Text("Здесь может быть размещена важная информация.")
            CloseButton()
        }
    }
}