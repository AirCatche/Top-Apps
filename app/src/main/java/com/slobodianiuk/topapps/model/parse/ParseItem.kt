package com.slobodianiuk.topapps.model.parse

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

class ParseItem {

    private val tag = "Parse application"
    val application: ArrayList<FeedEntry> = arrayListOf()


    fun parse(xmlData: String) : Boolean {
        var status = true
        var gotImage = false
        var currentEntry: FeedEntry? = null
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser().also {
                it.setInput(StringReader(xmlData))
            }
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name
                when(eventType) {
                    XmlPullParser.START_TAG -> {
                        if ("entry" == tagName) {
                            inEntry = true
                            currentEntry = FeedEntry()
                        } else if ("image" == tagName && inEntry) {
                            val imageResolution = xpp.getAttributeValue(null, "height")
                            if (imageResolution != null) { gotImage = "53" == imageResolution }
                        }
                    }
                    XmlPullParser.TEXT -> {
                        textValue = xpp.text
                    }
                    XmlPullParser.END_TAG -> {
                        if (inEntry) {
                            when(tagName) {
                                "entry" -> {
                                    currentEntry?.let {
                                        application.add(it)
                                    }
                                    inEntry = false
                                }
                                "name" -> {
                                    currentEntry?.name = textValue
                                }
                                "artist" -> {
                                    currentEntry?.artist = textValue
                                }
                                "releaseDate" -> {
                                    currentEntry?.releaseData = textValue
                                }
                                "price" -> {
                                    currentEntry?.price = textValue
                                    if (textValue == "Get") {
                                        currentEntry?.entryType = 1
                                    }
                                }
                                "summary" -> {
                                    currentEntry?.summary = textValue
                                }
                                "image" -> {
                                    currentEntry?.imageURL = textValue
                                }
                            }
                            //break
                        }
                    }
                    else -> {
                        print("BLYA") }
                }
                eventType = xpp.next()
            }


            for (app in application) {
                println("--------------------------")
                Log.d(tag, app.toString())
            }
        } catch (e: Exception) {
            status = false
            e.printStackTrace()
        }
        return status
    }
}