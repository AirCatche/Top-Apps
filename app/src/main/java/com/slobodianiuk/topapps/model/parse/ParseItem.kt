package com.slobodianiuk.topapps.model.parse

import android.util.Log
import com.slobodianiuk.topapps.model.itemRecycler.FeedAdapter
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

class ParseItem {
    private val tag = "Parse application"
    val application: ArrayList<FeedEntry> = arrayListOf()

    fun parse(xmlData: String) : Boolean {
        var status = true
        var inEntry = false
        var curEntry: FeedEntry? = null
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
                            curEntry = FeedEntry(curEntry?.name, curEntry?.artist, curEntry?.releaseData, curEntry?.summary, curEntry?.imageUrl, curEntry?.price, curEntry?.entryType)
                        }
                    }
                    XmlPullParser.TEXT -> {
                        textValue = xpp.text
                    }
                    XmlPullParser.END_TAG -> {
                        if (inEntry) {
                            when(tagName) {
                                "entry" -> {
                                    if (curEntry != null) {
                                        application.add(curEntry)
                                    }
                                    inEntry = false
                                }
                                "name" -> {
                                    curEntry  = curEntry?.copy(textValue,curEntry.artist,curEntry.releaseData,curEntry.summary,curEntry.imageUrl,curEntry.price,curEntry.entryType)
                                }
                                "artist" -> {
                                    curEntry  = curEntry?.copy(curEntry.name,textValue,curEntry.releaseData,curEntry.summary,curEntry.imageUrl,curEntry.price,curEntry.entryType)
                                }
                                "releaseDate" -> {
                                    curEntry  = curEntry?.copy(curEntry.name,curEntry.artist,textValue,curEntry.summary,curEntry.imageUrl,curEntry.price,curEntry.entryType)
                                }
                                "price" -> {
                                    curEntry  = curEntry?.copy(curEntry.name,curEntry.artist,curEntry.releaseData,curEntry.summary,curEntry.imageUrl,textValue,curEntry.entryType)
                                    if (textValue == "Get") {
                                        curEntry  = curEntry?.copy(curEntry.name,curEntry.artist,curEntry.releaseData,curEntry.summary,curEntry.imageUrl,curEntry.price,FeedAdapter.FEED_TYPE_FREE)
                                    } else {
                                        curEntry  = curEntry?.copy(curEntry.name,curEntry.artist,curEntry.releaseData,curEntry.summary,curEntry.imageUrl,curEntry.price,FeedAdapter.FEED_TYPE_PAID)
                                    }
                                }
                                "summary" -> {
                                    curEntry  = curEntry?.copy(curEntry.name,curEntry.artist,curEntry.releaseData,textValue,curEntry.imageUrl,curEntry.price,curEntry.entryType)
                                }
                                "image" -> {
                                    curEntry  = curEntry?.copy(curEntry.name,curEntry.artist,curEntry.releaseData,curEntry.summary,textValue,curEntry.price,curEntry.entryType)
                                }
                            }
                        }
                    }
                    else -> { }
                }
                eventType = xpp.next()
            }
        } catch (e: Exception) {
            status = false
            e.printStackTrace()
        }
        return status
    }
}