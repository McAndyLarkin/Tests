package test

import test.questions.Question

class TestRepository {
    private val testList: List<Test> by lazy {
        listOf(
            Test("1", "Test1", listOf(
                Question("where?", listOf("There", "hERE")),
                Question("wHO?", listOf("ME", "you", "he")),
            )),
            Test("2", "Test1", listOf(
                Question("Why?", listOf("So", "Do now")),
                Question("Tes?", listOf("yes", "da", "yeah")),
            )),
        )
    }
    fun findTestById(testId: String): Test? = testList.find { it.testId == testId }
}