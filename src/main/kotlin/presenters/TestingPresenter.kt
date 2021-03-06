package presenters

import data.delivering.FilesResolver
import data.delivering.OwnServerService
import models.test.Test

class TestingPresenter(private val dataDeliveringService: OwnServerService) {

    fun findTestById(testId: String): Test? =
        getTests().also(::println)
            .find { it.testId == testId }
            ?.also {
                print("save")
                FilesResolver.saveTestTo(it, "/Users/maksim/Downloads/tetstse.json")
            }

    fun add(test: Test) {
        dataDeliveringService.addTest(test)
        println("test added: $test")
    }

    fun getTests() = dataDeliveringService.tests
}