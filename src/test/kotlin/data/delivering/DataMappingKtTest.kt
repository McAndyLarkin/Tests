package data.delivering

import models.test.answers.Answer
import models.test.answers.AnswersSet
import org.json.simple.parser.JSONParser
import org.junit.Test

internal class DataMappingKtTest {

    @Test
    fun toMapToAnswer() {
        val set = AnswersSet(
            "test1",
            123456,
            "auth1",
            listOf(
                Answer.BinaryAnswer().apply { value = true },
                Answer.BinaryAnswer().apply { value = false },
                Answer.EnterableAnswer().apply { value = "VALUE" },
                Answer.EnterableAnswer().apply { value = "" },
                Answer.EnterableAnswer(),
                Answer.VariantsAnswer().apply { value = "Select" },
                Answer.VariantsAnswer().apply { value = "" },
                Answer.VariantsAnswer()
            )
        )
        println(set.toMap().toAnswer())
        println(set)
        assert(set.toMap() == set.toMap().toAnswer().toMap())
        assert(set == set.toMap().toAnswer())
    }

    @Test
    fun typicalAnswer() {
//        val ans = "[{\"testId\":\"85d517a2-9c90-4089-8f87-688bb6df45e0\",\"author\":\"anonymous\",\"id\":31,\"count\":20,\"answers\":[{\"number\":1,\"value\":true,\"type\":Binary},{\"number\":2,\"value\":false,\"type\":Binary},{\"number\":3,\"value\":null,\"type\":Binary},{\"number\":4,\"value\":null,\"type\":Binary},{\"number\":5,\"value\":null,\"type\":Binary},{\"number\":6,\"value\":null,\"type\":Binary},{\"number\":7,\"value\":null,\"type\":Binary},{\"number\":8,\"value\":null,\"type\":Binary},{\"number\":9,\"value\":null,\"type\":Binary},{\"number\":10,\"value\":null,\"type\":Binary},{\"number\":11,\"value\":null,\"type\":Binary},{\"number\":12,\"value\":null,\"type\":Binary},{\"number\":13,\"value\":null,\"type\":Binary},{\"number\":14,\"value\":null,\"type\":Binary},{\"number\":15,\"value\":null,\"type\":Binary},{\"number\":16,\"value\":null,\"type\":Binary},{\"number\":17,\"value\":null,\"type\":Binary},{\"number\":18,\"value\":null,\"type\":Binary},{\"number\":19,\"value\":null,\"type\":Binary},{\"number\":20,\"value\":null,\"type\":Binary}]}]"
//        val ans = "[{\"testId\":\"85d517a2-9c90-4089-8f87-688bb6df45e0\",\"author\":\"anonymous\",\"id\":31,\"count\":20,\"answers\":[{\"number\":1,\"value\":true,\"type\":Binary}]}]"
//        val ans = "[{\"testId\":\"85d517a2-9c90-4089-8f87-688bb6df45e0\",\"author\":\"anonymous\",\"id\":31,\"count\":20,\"answers\":[]}]"
        val ans = "[{\"testId\":\"85d517a2-9c90-4089-8f87-688bb6df45e0\",\"author\":\"anonymous\",\"id\":31,\"count\":20,\"answers\":[{\"number\":1,\"value\":\"true\",\"type\":\"Binary\"},{\"number\":2,\"value\":\"false\",\"type\":\"Binary\"},{\"number\":3,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":4,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":5,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":6,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":7,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":8,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":9,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":10,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":11,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":12,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":13,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":14,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":15,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":16,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":17,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":18,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":19,\"value\":\"null\",\"type\":\"Binary\"},{\"number\":20,\"value\":\"null\",\"type\":\"Binary\"},]},]"
        JSONParser().parse(ans)
    }

}