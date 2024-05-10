package com.limheejin.kakaoimagesearch.model

import com.google.gson.annotations.SerializedName

data class ImageSearchResponse<T>(
    @SerializedName("meta")
    val metaData: MetaData?,

    @SerializedName("documents")
    var documents: MutableList<T>?
)