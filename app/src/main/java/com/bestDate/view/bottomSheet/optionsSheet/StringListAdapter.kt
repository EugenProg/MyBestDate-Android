package com.bestDate.view.bottomSheet.optionsSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemBottomSheetListBinding
import com.bestDate.presentation.base.BaseClickViewHolder

class StringListAdapter(
    private val items: MutableList<String>,
    private val itemClick: (String) -> Unit
) :
    RecyclerView.Adapter<StringListAdapter.StringListViewHolder>() {

    class StringListViewHolder(override val binding: ItemBottomSheetListBinding) :
        BaseClickViewHolder<String, (String) -> Unit, ItemBottomSheetListBinding>(binding) {

        override fun bindView(item: String, itemClick: (String) -> Unit) {
            binding.name.text = item

            itemView.setOnSaveClickListener {
                itemClick.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringListViewHolder {
        return StringListViewHolder(
            ItemBottomSheetListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StringListViewHolder, position: Int) {
        holder.bind(items[position], itemClick)
    }

    override fun getItemCount(): Int = items.size
}