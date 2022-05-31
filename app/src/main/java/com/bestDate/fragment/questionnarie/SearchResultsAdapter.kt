package com.bestDate.fragment.questionnarie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.base.BaseClickViewHolder
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemSearchResultBinding

class SearchResultsAdapter(private var itemClick: (String) -> Unit):
    ListAdapter<String, SearchResultsAdapter.SearchViewHolder>(SearchResultCallback()) {

    private class SearchResultCallback: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    class SearchViewHolder(override var binding: ItemSearchResultBinding):
        BaseClickViewHolder<String, (String) -> Unit, ItemSearchResultBinding>(binding) {
        override fun bindView(item: String, itemClick: (String) -> Unit) {
            binding.name.text = item
            binding.root.setOnSaveClickListener { itemClick.invoke(item) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position), itemClick)
    }
}