package models.test.questions

open class Answer<T> {
    var value: T? = null

    class BinaryAnswer : Answer<Boolean>()
    class VariantsAnswer : Answer<String>()
    class EnterableAnswer : Answer<String>()
}