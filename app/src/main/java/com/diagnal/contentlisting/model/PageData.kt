package com.diagnal.contentlisting.model

import android.text.SpannableString
import com.google.gson.annotations.SerializedName

//    "title": "Romantic Comedy",
//    "total-content-items" : "54",
//    "page-num" : "1",
//    "page-size" : "20",
//    "content-items"
data class PageData(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("total-content-items")
    val totalItems: String = "",
    @SerializedName("page-num")
    val pageNum: String = "",
    @SerializedName("page-size")
    val pageSize: String = "",
    @SerializedName("content-items")
    val contentList: List<ContentData>
)

data class ContentData(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("poster-image")
    val posterImage: String = "",
    var highlightedText: SpannableString?
)

