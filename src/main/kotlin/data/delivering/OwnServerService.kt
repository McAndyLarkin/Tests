package data.delivering

import models.test.Test
import models.test.answers.AnswersSet
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.net.ConnectException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


class OwnServerService : DataDeliveringService {
    private val client: HttpClient = HttpClient.newBuilder().build()
    private val jsonParser = JSONParser()

    override fun addTest(test: Test) {

        val requestBody = JSONObject(test.toMap()).toJSONString()

        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create(TESTS_URL))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println("Post response: ${response.statusCode()}")
    }

    override val tests: List<Test>
        get() = HttpRequest.newBuilder()
            .uri(URI.create(TESTS_URL)).GET().build().let { request ->
                return try {
                    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
                    println("Get response: ${response.statusCode()}")
                    response.body()
                        .let { stream -> jsonParser.parse(stream) as JSONArray }
                        .map{
                            val testObj = it as JSONObject
                            testObj.toTest()
                        }

                } catch (e: java.net.ConnectException) {
                    println("Maybe invalid request")
                    e.printStackTrace()
                    listOf()
                } catch (e: org.json.simple.parser.ParseException) {
                    println("Parse Error")
                    e.printStackTrace()
                    listOf()
                }
            }

    override fun addAnswer(answers: AnswersSet) {

        val requestBody = JSONObject(answers.toMap()).toJSONString()
        println("req: $requestBody")

        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create(ANSWERS_URL))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println("Post response: ${response.statusCode()}")
    }

    override fun login(login: String, password: String): Boolean {
        val requestBody = JSONObject(mapOf(
            "login" to login,
            "password" to password,
        )).toJSONString()

        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create(LOGIN_URL))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()
        try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            println("Post response: ${response.statusCode()}")
            return response.statusCode() == 200
        } catch (conEx: ConnectException) {
            println("Connection Failed: ${conEx.message}")
            return false
        }
    }

    override val answers: List<AnswersSet>
        get() = HttpRequest.newBuilder()
            .uri(URI.create(ANSWERS_URL)).GET().build().let { request ->
                return try {
                    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
                    println("Get response: ${response.statusCode()}")
                    response.body()
                        .also { println("str $it") }
                        .let { stream -> jsonParser.parse(stream) as JSONArray }
                        .map{
                            val ans = it as JSONObject
                            ans.toAnswer()
                        }

                } catch (e: java.net.ConnectException) {
                    println("Maybe invalid request")
                    e.printStackTrace()
                    listOf()
                } catch (e: org.json.simple.parser.ParseException) {
                    println("Parse Error")
                    e.printStackTrace()
                    listOf()
                }
            }

    companion object {
        private const val SERVER_URL = "127.0.0.1:8888"
        const val TESTS_URL = "http://$SERVER_URL/pages/tests.php"
        const val ANSWERS_URL = "http://$SERVER_URL/pages/answers.php"
        const val LOGIN_URL = "http://$SERVER_URL/pages/login.php"
    }
}