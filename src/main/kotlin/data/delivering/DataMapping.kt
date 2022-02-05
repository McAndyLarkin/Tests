package data.delivering

import models.test.Test
import models.test.questions.Question

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
                    set("rightAnswer", it.type.rightAnswer ?: "null")
                    when (it.type) {
                        is Question.Type.BINARY -> {
                            set("positive", it.type.positive)
                            set("negative", it.type.negative)
                        }
                        is Question.Type.VARIANTS -> {
                            set("poly", it.type.poly)
                            set("variants", it.type.variants)
                        }
                    }
                }
            ))
        }
    }
)

//fun Map<*, *>.toTest() = Test(
//    get("testId") as ,
//    get("name"),
//    get("subtitle"),
//    get("description"),
//    get("questions"),
//)