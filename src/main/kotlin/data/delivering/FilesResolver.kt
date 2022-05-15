package data.delivering

import models.test.Test
import models.test.questions.Question
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import org.json.simple.parser.ParseException.ERROR_UNEXPECTED_TOKEN
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*


object FilesResolver {
    fun getTestFrom(path: String, onErrorAction: ((Exception, String) -> Unit)? = null): Test? {
        val jsonParser = JSONParser()

        try {
            FileReader(path).use { reader ->
                val testObject = jsonParser.parse(reader) as JSONObject
                println(testObject)

                val name = testObject["name"] as String
                val testId = UUID.randomUUID().toString()
                val subtitle = testObject["subtitle"] as String?
                val description = testObject["description"] as String

                val questions = mutableListOf<Question>()
                val questionsObject = testObject["questions"] as JSONArray
                questionsObject.indices.forEach { index ->
                    (questionsObject[index] as JSONObject).let { questionObject ->
                        questions.add(
                            Question(
                                index + 1,
                                questionObject["title"] as String,
                                getQuestionTypeFrom(questionObject["type"] as JSONObject)
                            )
                        )
                    }
                }

                return Test(testId, name, subtitle, description, questions)
            }
        } catch (e: FileNotFoundException) {
            onErrorAction?.let {
                onErrorAction(e, "File is not exist")
            }
            return null
        } catch (e: ParseException) {
            onErrorAction?.let {
                onErrorAction(e, "Bad file format")
            }
            return null
        } catch (e: Exception) {
            onErrorAction?.let {
                e.printStackTrace()
                onErrorAction(e, "Unknown issue")
            }
            return null
        }
    }

    fun saveTestTo(test: Test, path: String) {
        val testObject = JSONObject()
        testObject["name"] = test.name
        testObject["subtitle"] = test.subtitle
        testObject["description"] = test.description

        val questionsObject = JSONArray()
        test.questions.indices.forEach { index ->
            val question = test.questions[index]
            questionsObject.add(
                JSONObject().apply {
                    set("number", index + 1)
                    set("title", question.title)
                    set("type", getJSONObjectFrom(question.type))
                }
            )
        }

        testObject["questions"] = questionsObject

        try {
            FileWriter(path).use { file ->
                file.write(testObject.toJSONString())
                file.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

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
                throw ParseException(ERROR_UNEXPECTED_TOKEN)
            }
        }
    }

    private fun getJSONObjectFrom(type: Question.Type<*>): JSONObject {
        val typeObj = JSONObject()
        typeObj["name"] = type::class.java.simpleName
        when (type) {
            is Question.Type.BINARY -> {
                typeObj["positive"] = type.positive
                typeObj["negative"] = type.negative
            }
            is Question.Type.VARIANTS -> {
                typeObj["variants"] = JSONArray().apply {
                    type.variants.forEach(this::add)
                }
            }
        }
        return typeObj
    }
}