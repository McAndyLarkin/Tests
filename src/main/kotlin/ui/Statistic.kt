package ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import models.test.answers.Answer
import models.test.questions.Question
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.TextLine
import org.jetbrains.skia.Typeface
import repositories.SingletonCenter
import ui.helpers.ColorsHelper
import ui.helpers.RatiosHelper
import ui.viewmodels.ticket.getAnswerVal
import kotlin.math.ceil

const val columns = 5

@Composable
fun StatisticPage(testId: String) {
    Column(Modifier.width(RatiosHelper.getMainContentWidth().dp).padding(20.dp)) {
        SingletonCenter.testRepository.findTestById(testId)?.let { test ->
            Row(Modifier) {
                Column {
                    Header()
                    Text("Test: ${test.name}.", fontSize = 22.sp)
                }
                CloseButton()
            }
            SingletonCenter.answerRepository.findAnswersByTestId(testId).let { answerSets ->
                Text("Total answers: ${answerSets.size}")
                val rows = ceil(test.questions.size / columns.toDouble()).toInt()
                Column (modifier = Modifier
                    .background(color = Color(.97f,1f, 1f, 1f)).padding(4.dp)
                    .verticalScroll(rememberScrollState())) {
                    for (r in 0 until  rows) {
                        Row {
                            for (c in 0 until columns) {
                                val questionIndex = r * columns + c
                                if (questionIndex < test.questions.size) {
                                    StatistCeil(test.questions[questionIndex],
                                        answerSets.map { set -> set.answers[questionIndex] }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

val ceilWidth = RatiosHelper.getMainContentWidth() / columns
val ceilHeight = ceilWidth * 1.6
@Composable
private fun StatistCeil(question: Question, answers: List<Answer<*>>) {
    Column(Modifier.width(ceilWidth.dp)
        .height(ceilHeight.dp)
        .background(Color(.75f,1f, 1f, 1f))
        .padding(3.dp)
        .border(4.dp, Color(.97f,1f, 1f, 1f))
        .padding(10.dp)) {
        Text(question.title, softWrap = true, color = Color.Magenta)
        val grouped_by_count = answers
            .groupBy(Answer<*>::value)
            .entries.toList()
            .filter { entry -> entry.key != null }
        if (grouped_by_count.isNotEmpty()) {
            val size = DpSize((ceilHeight * 0.4).dp, (ceilHeight * 0.4).dp)
            if (grouped_by_count.size == 1) {
                Text(
                    "The only answer provided: ${
                        getAnswerVal(question, grouped_by_count.first().value.first())
                    }", softWrap = true
                )
            } else if (question.type is Question.Type.VARIANTS) {
                val most = grouped_by_count.maxByOrNull { it.value.size }!!
                val least = grouped_by_count.minByOrNull { it.value.size }!!
                Text(
                    "The most popular: ${
                        getAnswerVal(question, most.value.first())
                    }; ${"%.2f".format((most.value.size / answers.size.toDouble()) * 100)}%; ${most.value.size} pcs", softWrap = true
                )
                Text(
                    "The least popular: ${
                        getAnswerVal(question, least.value.first())
                    }; ${"%.2f".format((least.value.size / answers.size.toDouble()) * 100)}%; ${least.value.size} pcs", softWrap = true
                )
                Spacer(modifier = Modifier.height(25.dp))
                drawPlot(grouped_by_count.map { Pair(getAnswerVal(question, it.value.first()), it.value.size.toDouble()) }, size = size, true)
            } else if (question.type is Question.Type.BINARY) {
                val most = grouped_by_count.maxByOrNull { it.value.size }!!
                val least = grouped_by_count.minByOrNull { it.value.size }!!
                if (most.value.size != least.value.size) {
                    Text(
                        "Answer '${
                            getAnswerVal(question, most.value.first())
                        }' (${most.value.size} pcs) faces more often than '${
                            getAnswerVal(question, least.value.first())
                        }' (${least.value.size} pcs)", softWrap = true
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    drawDia(grouped_by_count.map { Pair(getAnswerVal(question, it.value.first()), it.value.size.toDouble()) }, size = size)
                } else {
                    Text(
                        "Answers '${question.type.positive}' and '${question.type.negative}' " +
                                "faces the same often (${least.value.size} pcs)", softWrap = true
                    )
                }
            } else if (question.type is Question.Type.ENTERABLE) {
                grouped_by_count.forEach { answer ->
                    Text(
                        "* ${
                            getAnswerVal(question, answer.value.first())
                        }; ${"%.2f".format((answer.value.size / answers.size.toDouble()) * 100)}%; ${answer.value.size} pcs",
                        softWrap = true
                    )
                }
            } else if (question.type is Question.Type.NUM_ENTERABLE ) {
                val double_answer = answers.filter { it.value is Double }.map { it as Answer<Double> }
                val max = double_answer.maxByOrNull { it.value ?: 0.0 }
                val min = double_answer.minByOrNull { it.value ?: 0.0 }
                val average = (double_answer.sumOf { it.value ?: 0.0 } / double_answer.size.toDouble())
                    .toString().let { it.substring(0, it.length.coerceAtMost(8)) }
                val median: String? = double_answer.sortedBy { it.value ?: 0.0 }.let {
                    if (it.size % 2 != 0) {
                        it[double_answer.size / 2].value
                    } else if (double_answer.isNotEmpty()) {
                        val left = it[(double_answer.size / 2) - 1].value
                        val right = it[double_answer.size / 2].value
                        if (left != null && right != null) (left + right) / 2.0
                        else left ?: right
                    } else null
                }?.toString()?.let { it.substring(0, it.length.coerceAtMost(8)) }

                Text("The biggest answer: ${max?.value ?: "Undefined"}", softWrap = true)
                Text("The smallest answer: ${min?.value ?: "Undefined"}", softWrap = true)
                Text("Average: $average", softWrap = true)
                Text("Median: ${median ?: "Undefined"}", softWrap = true)
                Spacer(modifier = Modifier.height(25.dp))
                drawPlot(double_answer.map {
                    println("v - ${it.value}")
                    Pair(it.value!!.toString().let { it.substring(0, it.length.coerceAtMost(8)) }, it.value!!)
                }, size = size, false)
            }
        } else {
            Text("There is not answers for the question", softWrap = true)
        }
    }
}

@Composable
fun drawPlot(grouped: List<Pair<String?, Double>>, size: DpSize, shallSpecify: Boolean) {
    Canvas(modifier = Modifier.size(size = size)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val max = grouped.maxByOrNull { it.second }!!

        val segment_w = canvasWidth / (grouped.size - 1)
        val segment_h = canvasHeight / (grouped.size - 1)
        for (group_i in grouped.indices) {
            val h = (canvasHeight * (grouped[group_i].second / max.second).toFloat())
            drawCircle(Color.Red,10f, center = Offset(x = (segment_w * group_i).toPx(),
                y = (canvasHeight - h).toPx()))
            drawContext.canvas.nativeCanvas.apply {
                drawTextLine(line = TextLine.Companion.make(if (shallSpecify) "${grouped[group_i].first}: ${grouped[group_i].second}"
                    else grouped[group_i].first, Font(Typeface.makeDefault(), 26f)),
                    paint = Paint().apply {
                        this.strokeWidth = 10f
                    }, x = (segment_w * group_i).toPx(), y = (canvasHeight-h).toPx())
            }
        }

        //ox
        drawLine(
            start = Offset(x = 0f, y = canvasHeight.toPx()),
            end = Offset(x = canvasWidth.toPx(), y = canvasHeight.toPx()),
            color = Color.Black,
            strokeWidth = 5F
        )

        //oy
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = 0f, y = canvasHeight.toPx()),
            color = Color.Black,
            strokeWidth = 5F
        )

    }
}
@Composable
fun drawDia(grouped: List<Pair<String?, Double>>, size: DpSize) {
    Canvas(modifier = Modifier.size(size = size)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val max = grouped.maxByOrNull { it.second }!!

        val segment_w = canvasWidth / (grouped.size)
        val segment_h = canvasHeight / (grouped.size)
        var a = false;
        for (group_i in grouped.indices) {
            val h = (canvasHeight * (grouped[group_i].second / max.second).toFloat())
            drawRect((if (a) Color.Red else Color.Yellow),
                Offset(x = (segment_w * group_i).toPx(), y = (canvasHeight - h).toPx()),
                Size(segment_w.toPx(), (h).toPx())
            )
            drawContext.canvas.nativeCanvas.apply {
                drawTextLine(line = TextLine.Companion.make(grouped[group_i].second.toString(), Font(Typeface.makeDefault(), 34f)),
                    paint = Paint().apply {
                        this.strokeWidth = 10f
                    }, x = (segment_w * group_i + 5.dp).toPx(), y = (canvasHeight-h-5.dp).toPx())
            }
            drawContext.canvas.nativeCanvas.apply {
                drawTextLine(line = TextLine.Companion.make(grouped[group_i].first, Font(Typeface.makeDefault(), 34f)),
                    paint = Paint().apply {
                        this.strokeWidth = 10f
                    }, x = (segment_w * group_i + 5.dp).toPx(), y = canvasHeight.toPx() + 35f)
            }
            a = a.not()
        }

        //ox
        drawLine(
            start = Offset(x = 0f, y = canvasHeight.toPx()),
            end = Offset(x = canvasWidth.toPx(), y = canvasHeight.toPx()),
            color = Color.Black,
            strokeWidth = 5F
        )

        //oy
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = 0f, y = canvasHeight.toPx()),
            color = Color.Black,
            strokeWidth = 5F
        )

    }
}



@Composable
private fun Header() {
    Column(Modifier.padding(bottom = 20.dp)) {
        Text("Statistic", fontSize = 28.sp, color = ColorsHelper.PASS_TEST_BUTTON)
    }
}