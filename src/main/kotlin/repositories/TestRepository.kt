package repositories

import models.test.Test
import models.test.questions.Question

class TestRepository {
    val testList: List<Test> by lazy {
        listOf(
            Test("1", "Test1", listOf(
                Question(1, "where?", Question.Type.BINARY("Yes", "No")),
                Question(2, "wHO?", Question.Type.VARIANTS(listOf("ME", "you", "he"))),
                Question(3, "Name?", Question.Type.ENTERABLE()),
            )),
            Test("2", "Test1", listOf(
                Question(1, "Why?", Question.Type.VARIANTS(listOf("So", "Do now"))),
                Question(2, "Tes?", Question.Type.VARIANTS(listOf("yes", "da", "yeah"))),
            )),
        )
    }
    fun findTestById(testId: String): Test? = testList.find { it.testId == testId }
}