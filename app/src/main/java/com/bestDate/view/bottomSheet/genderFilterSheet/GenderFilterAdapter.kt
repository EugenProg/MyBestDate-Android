package com.bestDate.view.bottomSheet.genderFilterSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemBottomSheetListBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bestDate.presentation.main.search.GenderFilter

class GenderFilterAdapter(
    private val items: Array<GenderFilter>,
    private val selectedItem: GenderFilter
) :
    RecyclerView.Adapter<GenderFilterAdapter.GenderListViewHolder>() {

    var itemClick: ((GenderFilter) -> Unit)? = null

    class GenderListViewHolder(
        override val binding: ItemBottomSheetListBinding,
        private val selectedItem: GenderFilter
    ) : BaseClickViewHolder<GenderFilter,
            ((GenderFilter) -> Unit)?,
            ItemBottomSheetListBinding>(binding) {

        override fun bindView(item: GenderFilter, itemClick: ((GenderFilter) -> Unit)?) {
            binding.name.text = itemView.context.getString(item.filterName)
            binding.check.isVisible = item == selectedItem

            itemView.setOnSaveClickListener {
                itemClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderListViewHolder {
        return GenderListViewHolder(
            ItemBottomSheetListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), selectedItem
        )
    }

    override fun onBindViewHolder(holder: GenderListViewHolder, position: Int) {
        holder.bind(items[position], itemClick)
    }

    override fun getItemCount(): Int = items.size
}