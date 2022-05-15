package data.delivering

import models.test.Test
import models.test.answers.Answer
import models.test.answers.AnswersSet
import models.test.questions.Question
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.ParseException
import java.util.*

fun Test.toMap() = mapOf(
    //Need to unite this method and FilesProvider
    "testId" to testId,
    "name" to name,
    "subtitle" to subtitle,
    "description" to description,
    "questions" to mutableListOf<Map<String, Any>>().apply {
        questions.forEach {
            add(mapOf(
                "number" to it.number,
                "title" to it.title,
                "type" to mutableMapOf<String, Any>().apply {
                    set("name", it.type::class.java.simpleName)
                    when (it.type) {
                        is Question.Type.BINARY -> {
                            set("positive", it.type.positive)
                            set("negative", it.type.negative)
                        }
                        is Question.Type.VARIANTS -> {
                            set("variants", it.type.variants)
                        }
                    }
                }
            ))
        }
    }
)
fun AnswersSet.toMap() = mapOf(
    //Need to unite this method and FilesProvider
    "testId" to testId,
    "id" to id,
    "author" to author,
    "count" to answers.size,
    "answers" to mutableListOf<Map<String, Any?>>().apply {
        for (i in answers.indices){
            add(mapOf(
                "type" to when (answers[i]) {
                    is Answer.BinaryAnswer -> "Binary"
                    is Answer.EnterableAnswer -> "Enter"
                    is Answer.VariantsAnswer -> "Variant"
                    is Answer.NumEnterableAnswer -> "NumEnter"
                    else -> "?"
                },
                "number" to i+1,
                "value" to answers[i].value?.toString() as String?
            ))
        }
    }
)

fun Map<*, *>.toTest() = Test(
    get("testId") as String,
    get("name") as String,
    get("subtitle") as String?,
    get("description") as String,
    get("questions").let { it -> val questions = it as JSONArray
        questions.map { it -> val question = it as JSONObject
            Question(
                (question["number"] as Long).toInt(),
                question["title"] as String,
                getQuestionTypeFrom(question["type"] as JSONObject)
            )
        }
    },
)

fun Map<*, *>.toAnswer() = AnswersSet(
    get("testId") as String,
    get("id") as Long?,
    get("author") as String?,
    get("answers").let { ans -> val answers = ans as Iterable<*>
        answers.map { an -> val answer = an as Map<*,*>
            when (answer["type"] as String) {
                "Binary" -> {
                    Answer.BinaryAnswer(

                    ).apply {
                        value = answer["value"]
                            ?.let { it as String }
                            .let { if (it == "null") null
                            else it.toBoolean()}
                    }
                }
                "Enter" -> Answer.EnterableAnswer(

                ).apply {
                    value = answer["value"]
                                ?.let { it as String }
                                .let { if (it == "null") null
                                else it}
                }
                "Variant" -> Answer.VariantsAnswer(

                ).apply {
                    value = answer["value"]
                                ?.let { it as String }
                                .let { if (it == "null") null
                                else it}
                }

                "NumEnter" -> Answer.NumEnterableAnswer().apply {
                    value = answer["value"]
                        ?.let { it as String }
                        .let { if (it == "null") null
                        else it?.toDouble()}
                }

                else -> throw Exception("It no compatable AnswerType")
            }
        }
    },
)


private fun getQuestionTypeFrom(typeObj: JSONObject): Question.Type<*> {
    val name = typeObj["name"]
    return when(name) {
        Question.Type.BINARY::class.java.simpleName -> {
            Question.Type.BINARY(
                typeObj["positive"] as String,
                typeObj["negative"] as String
            )
        }
        Question.Type.VARIANTS::class.java.simpleName -> {
            Question.Type.VARIANTS(
                mutableListOf<String>().apply {
                    (typeObj["variants"] as JSONArray).let{ array ->
                        array.forEach { variant ->
                            add(variant as String)
                        }
                    }
                },
                false
            )
        }
        Question.Type.ENTERABLE::class.java.simpleName -> {
            Question.Type.ENTERABLE()
        }
        Question.Type.NUM_ENTERABLE::class.java.simpleName -> {
            Question.Type.NUM_ENTERABLE()
        }
        else -> {
            throw ParseException(ParseException.ERROR_UNEXPECTED_TOKEN)
        }
    }
}