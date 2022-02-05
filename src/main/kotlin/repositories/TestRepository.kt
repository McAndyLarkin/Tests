package repositories

import data.delivering.FilesResolver
import data.delivering.FirebaseService
import models.test.Test
import models.test.questions.Question

class TestRepository {
    val dataDeliveringService = FirebaseService()
    private val testsSet: MutableSet<Test> by lazy {
        mutableSetOf(
            Test("1", "Test1", "Subtitle", "Description", listOf(
                Question(1, "where?", Question.Type.BINARY("Yes", "No")),
                Question(2, "wHO?", Question.Type.VARIANTS(listOf("ME", "you", "he"))),
                Question(3, "Name?", Question.Type.ENTERABLE()),
            )),
            Test("2", "Test2", "Subtitl2", "Description2", listOf(
                Question(1, "Why?", Question.Type.VARIANTS(listOf("So", "Do now"))),
                Question(2, "Tes?", Question.Type.VARIANTS(listOf("`yes", "da", "yeah"))),
            )),
        )
    }
    fun findTestById(testId: String): Test? = testsSet.find { it.testId == testId }?.also {
        print("save")
        FilesResolver.saveTestTo(it, "/Users/maksim/Downloads/tetstse.json")
    }

    fun add(test: Test) {
        testsSet.add(test)
        dataDeliveringService.addTest(test)
        println("added: $test")
    }

    fun getTests(): Set<Test> = testsSet
}