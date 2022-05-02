package ui

import actionManager
import actions.Action
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ui.helpers.RatiosHelper


@Composable
fun LoginPage() {
    val onLoginValue = remember { mutableStateOf(TextFieldValue("")) }
    val onPasswordValue = remember { mutableStateOf(TextFieldValue("")) }
    Row(Modifier.fillMaxWidth()) {
        Column(
            Modifier.fillMaxHeight()
                .width(RatiosHelper.getMainContentWidth().dp)
                .padding(20.dp)
        ) {
            OutlinedTextField(
                value = onLoginValue.value,
                onValueChange = { value: TextFieldValue ->
                    onLoginValue.value = value
                },
                modifier = Modifier.width(300.dp),
                label = { Text("Admin Login") },
            )
            OutlinedTextField(
                value = onPasswordValue.value,
                onValueChange = { value: TextFieldValue ->
                    onPasswordValue.value = value
                },
                modifier = Modifier.width(300.dp),
                label = { Text("Admin Password") },
            )
            Button(onClick = {
                actionManager.send(
                    Action.INTERNAL.LOG_IN(
                        login = onLoginValue.value.text,
                        password = onPasswordValue.value.text,
                    )
                )
            }) {
                Text("LogIn")
            }
        }
        CloseButton()
    }
}