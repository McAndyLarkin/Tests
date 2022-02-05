import data.delivering.FilesResolver
import org.junit.Test

class FilesResolverTest {

    @Test
    fun testSaveLoad() {
        val test = models.test.Test(
            "id", "name", "subtitle", "description",
            listOf(
                models.test.questions.Question(
                    1, "title1",
                    models.test.questions.Question.Type.BINARY(
                        "positive", "negative"
                    )
                ),
                models.test.questions.Question(
                    2, "title2",
                    models.test.questions.Question.Type.VARIANTS(
                        listOf("variant1", "variant2"), false
                    )
                ),
                models.test.questions.Question(
                    3, "title3",
                    models.test.questions.Question.Type.ENTERABLE()
                ),
            )
        )

        //Unix
        val path = System.getProperty("user.dir").plus("/testing.tests/test_Test.json")

        FilesResolver.saveTestTo(test, path)
        val newTest = FilesResolver.getTestFrom(path) { a, b ->
            println("$b: ${a.localizedMessage}")
            a.printStackTrace()
        }
        assert(newTest != null)

         newTest?.let {
             assert(newTest.equals(test))
         }
    }
}