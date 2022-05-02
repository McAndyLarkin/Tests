package repositories

import data.delivering.OwnServerService

object SingletonCenter {
    val dataDeliveringService = OwnServerService()
    val testRepository = TestRepository(dataDeliveringService)
    val feedRepository = FeedRepository()
    val appRepository = AppRepository()
    val authRepository = AuthRepository(dataDeliveringService)
    val answerRepository = AnswerRepository(dataDeliveringService)
}