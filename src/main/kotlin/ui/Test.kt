import actions.Action
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import localization.LocalizedStrings
import models.test.Test
import models.test.answers.Answer
import models.test.answers.AnswersSet
import models.test.questions.Question
import presenters.AnswerHolder
import presenters.SingletonCenter
import ui.CloseButton
import ui.backToFeed
import ui.helpers.ColorsHelper
import ui.helpers.RatiosHelper


@Composable
fun TestPage(testId: String) {
    SingletonCenter.testingPresenter.findTestById(testId)?.let { test ->
        val answers = AnswerHolder.forTest(test)
        Column(modifier = Modifier.width(RatiosHelper.getMainContentWidth().dp)
            .padding(start = 220.dp, top = 20.dp, bottom = 20.dp, end = 20.dp)
            .fillMaxHeight()) {
            Row(Modifier.fillMaxWidth()) {
                Header(test)
                CloseButton()
            }
            Column(Modifier.padding(end = 220.dp)
                .verticalScroll(rememberScrollState())) {
                for (question in test.questions) {
                    Question(question, answers)
                }
                Row {
                    SendButton(answers, testId)
                }
            }
        }
    } ?: Text("Тест не найден!")
}

@Composable
private fun Question(question: Question, answerHolder: AnswerHolder) {
    Text("Вопрос №${question.number}: ${question.title}",
        Modifier.padding(top = 10.dp), color = ColorsHelper.QUESTION_HEADER)
    Row {
        when (question.type) {
            is Question.Type.VARIANTS -> {
                val onOption = remember { mutableStateOf("none") }
                Column {
                    question.type.variants.forEach { option ->
                        Variant(onOption, answerHolder, option, question.number, question.type.poly, null)
                    }
                }
            }
            is Question.Type.BINARY -> {
                val onOption = remember { mutableStateOf("none") }
                Variant(onOption, answerHolder, question.type.positive, question.number, false, true)
                Variant(onOption, answerHolder, question.type.negative, question.number, false, false)
            }
            is Question.Type.ENTERABLE -> {
                val onOption = remember { mutableStateOf(TextFieldValue("")) }
                Enterable(onOption, answerHolder, question.number, false)
            }
            is Question.Type.NUM_ENTERABLE -> {
                val onOption = remember { mutableStateOf(TextFieldValue("")) }
                Enterable(onOption, answerHolder, question.number, true)
            }
        }
    }
}

@Composable
private fun Enterable(onOption: MutableState<TextFieldValue>, answerHolder: AnswerHolder, number: Int, onlyDigits: Boolean) {
    OutlinedTextField(
        value = onOption.value,
        onValueChange = { value: TextFieldValue ->
            val answer = answerHolder.answers[number - 1]
            if (answer is Answer.EnterableAnswer) {
                if (value.text.isEmpty()) {
                    onOption.value = TextFieldValue(text = "", TextRange.Zero)
                    answer.value = null
                }else {
                    onOption.value = value
                    answer.value = value.text
                }
            } else if (answer is Answer.NumEnterableAnswer && onlyDigits) {
                var digit_str = ""
                var has_dot = false
                for (i in value.text.indices) {
                    val ch = value.text[i]
                    if (ch.isDigit() || (ch == '-' && i == 0)) {
                        digit_str += ch
                    } else if (ch == '.') {
                        if (!has_dot) {
                            has_dot = true
                            digit_str += ch
                        }
                    }
                }
                digit_str.toDoubleOrNull()?.let {
                    onOption.value = TextFieldValue(text = digit_str, TextRange(digit_str.length))
                    answer.value = it
                } ?: if (digit_str.isEmpty() || digit_str == "-" || digit_str == "-.") {
                    onOption.value = TextFieldValue(text = "", TextRange.Zero)
                    answer.value = null
                }
            }
        },
        modifier = Modifier.width(300.dp), label = { Text("answer") }
    )
}

@Composable
private fun Variant(onOption: MutableState<String>, answerHolder: AnswerHolder, variant: String, number: Int, poly: Boolean, binary: Boolean?) {
    val (option, onSelected) = onOption
    Row(Modifier.width(300.dp)
            .selectable(
                selected = (option == variant),
                onClick = {
                    onSelected(variant)
                    val answer = answerHolder.answers[number-1]
                    if (answer is Answer.VariantsAnswer) {
                        answer.value = variant
                    } else if (answer is Answer.BinaryAnswer) {
                        answer.value = binary
                    }
                }
            )
            .padding(horizontal = 16.dp)
    ) {
        RadioButton(
            selected = (option == variant),
            onClick = {
                onSelected(variant)
                val answer = answerHolder.answers[number-1]
                if (answer is Answer.VariantsAnswer) {
                    answer.value = variant
                } else if (answer is Answer.BinaryAnswer) {
                    answer.value = binary
                }
            }
        )
        Text(text = variant,
            style = MaterialTheme.typography.body1.merge(),
            modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun SendButton(answerHolder: AnswerHolder, testId: String) {
    val warningMessage = remember { mutableStateOf("") }
    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.width(RatiosHelper.getMainContentWidth().dp)) {
        Text(warningMessage.value, color = Color.Red, modifier = Modifier.padding(top = 15.dp, end = 20.dp))
        Button(onClick = {
                print("AnswerFinal: "); answerHolder.answers.forEach { print("${it.value}, ") }.let { println() }
                if (answerHolder.answers.any { it.value == null }) {
                    warningMessage.value = "Некоторые вопросы пропущены!"
                } else {
                    sendAnswer(answerHolder.answers, testId)
                    backToFeed()
                }
            }, modifier = Modifier.align(Alignment.Top), colors = ColorsHelper.CLEAN_BUTTON_COLORS) {
            Text("Отправить")
        }
    }
}

@Composable
private fun Header(test: Test) {
    Column(Modifier.padding(bottom = 20.dp)) {
        Text(test.name, fontSize = 28.sp, color = ColorsHelper.PASS_TEST_BUTTON)
        Text("${test.questions.size} ${LocalizedStrings.instance.QUESTIONS_POSTFIX}", fontSize = 24.sp, color = ColorsHelper.PASS_TEST_BUTTON)
        Text(test.description, fontSize = 20.sp, color = ColorsHelper.PASS_TEST_BUTTON)
    }
}

private fun sendAnswer(answers: List<Answer<*>>, testId: String) {
    actionManager.send(Action.INTERNAL.ADD_ANSWER(AnswersSet(
        testId, null, SingletonCenter.authPresenter.user.userId, answers
    )))
}