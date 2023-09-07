package com.diagnal.contentlisting.repo

import com.diagnal.contentlisting.di.ContextProvider
import com.diagnal.contentlisting.model.PageData
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.io.InputStream
import java.nio.charset.Charset
import javax.inject.Singleton

@Singleton
class PageRepositoryImpl : IPageRepository {
    private val context = ContextProvider.context?.get()
    override fun loadJson(pageNum: Int): PageData? {
        var jsonString: String? = null
        val fileName = getFileName(pageNum)
        try {
            val inputStream: InputStream? = context?.assets?.open(fileName)
            val size: Int? = inputStream?.available()
            val buffer = size?.let { ByteArray(it) }
            inputStream?.read(buffer)
            inputStream?.close()
            jsonString = buffer?.let { String(it, Charset.defaultCharset()) }
        } catch (e: Exception) {
            Firebase.crashlytics.recordException(e)
            e.printStackTrace()
        }

        if (jsonString != null) {
            val gson = Gson()
            return gson.fromJson(jsonString, PageData::class.java)
        }

        return null

    }

    private fun getFileName(pageNum: Int): String {
        when (pageNum) {
            1 -> {
                return "page_one.json"
            }

            2 -> {
                return "page_two.json"
            }

            3 -> {
                return "page_three.json"
            }

            else -> {
                Firebase.crashlytics.log("No file Found for pageNum: $pageNum")
                return "invalid"
            }
        }
    }
}