package ui.viewmodels.ticket

import actionManager
import actions.Action
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import models.User
import models.test.answers.Answer
import models.test.questions.Question
import repositories.SingletonCenter
import ui.CloseButton
import ui.PageType
import ui.helpers.ColorsHelper
import ui.helpers.RatiosHelper
import kotlin.math.round

@Composable
fun AnswersPage() {
    Column(modifier = Modifier
        .width(RatiosHelper.getMainContentWidth().dp)
        .padding(20.dp)
        .verticalScroll(rememberScrollState())) {
        Row(Modifier.fillMaxWidth()) {
            Header()
            CloseButton()
        }
        SingletonCenter.answerRepository.getAnswers()
            .groupBy { it.testId }
            .forEach { ans ->
                SingletonCenter.testRepository.findTestById(ans.key)?.let { test ->
                    Text("Test: ${test.name}.", fontSize = 22.sp)
                    Button(onClick = {
                        actionManager.send(Action.UI.OPEN_PAGE(PageType.STATISTIC(test.testId)))
                    }, colors = ColorsHelper.CLEAN_BUTTON_COLORS) {
                        Text("Statistics")
                    }
                    Text("Answers:")
                    ans.value.forEach { set ->
                        Text("________________________")
                        if (set.author != User.Companion.ANONYMOUS.userId) {
                            Text("Author: ${set.author}")
                        } else {
                            Text("Author: Unknown")
                        }
                        for (i in set.answers.indices){
                            val question = test.questions[i]
                            val answer = set.answers[i]
                            Text("${i+1}. ${question.title}: ${
                                getAnswerVal(question, answer)
                            }")
                            Spacer(Modifier.height(5.dp))
                        }
                    }
                }
                Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun Header() {
    Column(Modifier.padding(bottom = 20.dp)) {
        Text("Answers", fontSize = 28.sp, color = ColorsHelper.PASS_TEST_BUTTON)
    }
}

fun getAnswerVal(question: Question, answer: Answer<*>): String =
    if (question.type is Question.Type.BINARY
        && answer is Answer.BinaryAnswer) {
        answer.value?.let { ans ->
            if (ans) question.type.positive
            else question.type.negative
        } ?: "Did not answered"
    } else if (question.type is Question.Type.ENTERABLE
        && answer is Answer.EnterableAnswer) {
        answer.value ?: "Did not answered"
    }else if (question.type is Question.Type.VARIANTS
        && answer is Answer.VariantsAnswer) {
        answer.value ?: "Did not answered"
    } else if (question.type is Question.Type.NUM_ENTERABLE && answer is Answer.NumEnterableAnswer) {
        answer.value?.toString()?.let { it.substring(0, it.length.coerceAtMost(8)) } ?: "Did not answered"
    } else "Data destroyed"
