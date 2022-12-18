package com.bestDate.view.searchLocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.base.BaseClickViewHolder
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.utils.CityListItem
import com.bestDate.databinding.ItemSearchResultBinding

class SearchResultsAdapter(private var style: SearchLocationView.SearchStyle):
    ListAdapter<CityListItem, SearchResultsAdapter.SearchViewHolder>(SearchResultCallback()) {

    var itemClick: ((CityListItem) -> Unit)? = null

    private class SearchResultCallback: DiffUtil.ItemCallback<CityListItem>() {
        override fun areItemsTheSame(oldItem: CityListItem, newItem: CityListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CityListItem, newItem: CityListItem): Boolean {
            return oldItem == newItem
        }
    }

    class SearchViewHolder(override var binding: ItemSearchResultBinding,
                           var style: SearchLocationView.SearchStyle):
        BaseClickViewHolder<CityListItem, ((CityListItem) -> Unit)?, ItemSearchResultBinding>(binding) {
        override fun bindView(item: CityListItem, itemClick: ((CityListItem) -> Unit)?) {
            binding.name.text = item.getLocation()
            binding.name.setTextColor(ContextCompat.getColor(itemView.context, style.itemColor))
            binding.root.setOnSaveClickListener {
                itemClick?.invoke(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), style
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position), itemClick)
    }
}