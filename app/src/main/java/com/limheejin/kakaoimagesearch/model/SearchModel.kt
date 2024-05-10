package com.limheejin.kakaoimagesearch.model

import android.os.Parcelable

@Parcelize
data class SearchModel(
    val id: String = UUID.randomUUID().toString(),
    val thumbnailUrl: String?,
    val siteName: String?,
    val datetime: Date?,
    val itemType: SearchListType,
    val isSaved: Boolean = false
) : Parcelable