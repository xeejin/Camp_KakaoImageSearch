package com.limheejin.kakaoimagesearch.model

import com.google.gson.annotations.SerializedName
import java.util.Date

//  변수이름을 다르게하고 싶거나, json 파일의 이름이 한글인 경우 @SerializedName 어노테이션을 사용하여 역/직렬화

data class ImageDocument(
    @SerializedName("display_sitename")
    val siteName : String,
    @SerializedName("doc_url")
    val docUrl: String?,
    @SerializedName("thumbnail_url")
    val thumbnailUrl : String?,
    @SerializedName("datetime")
    val dateTime : Date?

)