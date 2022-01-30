package localization

import java.util.*

interface LocalizedStrings {
    val APP_NAME: String

    companion object {
        private val rus by lazy { RussianLocalizedStrings() }

        private val en by lazy { InternationalizedLocalizedStrings() }

        var instance: LocalizedStrings = when(Locale.getDefault().toLanguageTag()) {
            "ru-RU" -> rus
            else -> en
        }
    }
}