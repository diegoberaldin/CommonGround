package com.github.diegoberaldin.commonground.core.l10n.usecase

import com.github.diegoberaldin.commonground.core.l10n.data.LocalizableString
import org.koin.core.annotation.Single
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.io.InputStreamReader

@Single
internal class DefaultParseXmlResourceUseCase : ParseXmlResourceUseCase {
    companion object {
        private const val ELEM_STRING = "string"
        private const val ATTR_NAME = "name"
    }

    override operator fun invoke(inputStream: InputStream): List<LocalizableString> {
        return runCatching {
            val res = mutableListOf<LocalizableString>()

            val xpp = XmlPullParserFactory.newInstance().apply {
                isNamespaceAware = true
            }.newPullParser()
            xpp.setInput(InputStreamReader(inputStream))
            var eventType: Int = xpp.eventType

            var insideStringElement = false
            var currentKey = ""
            var currentString = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (xpp.name == ELEM_STRING) {
                            insideStringElement = true
                            val attributeCount = xpp.attributeCount
                            for (i in 0..<attributeCount) {
                                val attrName = xpp.getAttributeName(i)
                                val attrValue = xpp.getAttributeValue(i)
                                if (ATTR_NAME == attrName) {
                                    currentKey = attrValue
                                }
                            }
                        }
                    }

                    XmlPullParser.TEXT -> {
                        if (insideStringElement) {
                            val content = xpp.text
                            currentString = content.sanitize()
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if (xpp.name == ELEM_STRING) {
                            insideStringElement = false
                        }
                        val segment = LocalizableString(
                            key = currentKey,
                            value = currentString,
                        )
                        res += segment
                    }
                }

                eventType = xpp.next();
            }
            res
        }.also {
            it.exceptionOrNull()?.printStackTrace()
        }.getOrElse {
            emptyList()
        }
    }

    private fun String.sanitize(): String {
        val substitutionList = listOf(
            "\\'" to "'",
            "\\n" to "\n",
        )
        return substitutionList.fold(this) { res, it ->
            res.replace(it.first, it.second)
        }
    }
}
