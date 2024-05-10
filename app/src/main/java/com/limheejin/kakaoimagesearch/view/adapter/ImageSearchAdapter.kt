package com.limheejin.kakaoimagesearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.limheejin.kakaoimagesearch.model.SearchModel
import com.limheejin.kakaoimagesearch.util.SearchDiffUtil

class ImageSearchAdapter(
    private val itemClickListener: (SearchModel) -> Unit
) : ListAdapter<SearchModel, ImageSearchAdapter.ViewHolder>(SearchDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ImageSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: ImageSearchItemBinding,
        private val itemClickListener: ((SearchModel) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchModel) = with(binding) {
            ivImage.loadImage(item.thumbnailUrl)
            tvImageSitename.text = item.siteName
            tvImageDatetime.text = FormatManager.formatDateToString(item.datetime)
            ivHeart.isVisible = item.isSaved
            ivImage.setOnClickListener {
                itemClickListener?.invoke(item)
            }
        }
    }

}