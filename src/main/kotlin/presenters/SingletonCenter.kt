package presenters

import data.delivering.OwnServerService

object SingletonCenter {
    val dataDeliveringService = OwnServerService()
    val testingPresenter = TestingPresenter(dataDeliveringService)
    val mainPresenter = MainPresenter()
    val authPresenter = AuthPresenter(dataDeliveringService)
    val answersPresenter = AnswersPresenter(dataDeliveringService)
}