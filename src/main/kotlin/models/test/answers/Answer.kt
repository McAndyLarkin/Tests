package models.test.answers

open class Answer<T> {
    var value: T? = null

    class BinaryAnswer : Answer<Boolean>()
    class VariantsAnswer : Answer<String>()
    class EnterableAnswer : Answer<String>()

    override fun toString(): String {
        return "Answer[$value] ${javaClass.simpleName}"
    }

    override fun equals(other: Any?): Boolean {
        return javaClass.simpleName == other?.javaClass?.simpleName
                && value == (other as? Answer<*>)?.value
    }
}