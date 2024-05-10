package com.limheejin.kakaoimagesearch.util

import androidx.recyclerview.widget.DiffUtil
import com.limheejin.kakaoimagesearch.model.SearchModel

object SearchDiffUtil : DiffUtil.ItemCallback<SearchModel>() {

    override fun areItemsTheSame(oldItem: SearchModel, newItem: SearchModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchModel, newItem: SearchModel): Boolean {
        return oldItem == newItem
    }
}