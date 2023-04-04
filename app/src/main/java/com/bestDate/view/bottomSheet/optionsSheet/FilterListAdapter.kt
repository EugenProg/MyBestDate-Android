package com.bestDate.view.bottomSheet.optionsSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemBottomSheetListBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bestDate.presentation.main.search.FilterType

class FilterListAdapter(
    private val items: MutableList<Pair<FilterType, String>>,
    private val selectedItem: FilterType,
    private val itemClick: (Pair<FilterType, String>) -> Unit
) : RecyclerView.Adapter<FilterListAdapter.StringListViewHolder>() {

    class StringListViewHolder(override val binding: ItemBottomSheetListBinding,
                               private val selectedItem: FilterType) :
        BaseClickViewHolder<Pair<FilterType, String>, (Pair<FilterType, String>) -> Unit, ItemBottomSheetListBinding>(binding) {

        override fun bindView(item: Pair<FilterType, String>, itemClick: (Pair<FilterType, String>) -> Unit) {
            binding.name.text = item.second
            binding.check.isVisible = item.first == selectedItem

            itemView.setOnSaveClickListener {
                itemClick.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringListViewHolder {
        return StringListViewHolder(
            ItemBottomSheetListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), selectedItem
        )
    }

    override fun onBindViewHolder(holder: StringListViewHolder, position: Int) {
        holder.bind(items[position], itemClick)
    }

    override fun getItemCount(): Int = items.size
}