package com.bestDate.view.bottomSheet.optionsSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemGenderListBinding

class StringListAdapter(
    private val items: MutableList<String>,
    private val itemClick: (String) -> Unit
) :
    RecyclerView.Adapter<StringListAdapter.StringListViewHolder>() {

    class StringListViewHolder(override val binding: ItemGenderListBinding) :
        BaseClickViewHolder<String, (String) -> Unit, ItemGenderListBinding>(binding) {

        override fun bindView(item: String, itemClick: (String) -> Unit) {
            binding.name.text = item

            itemView.setOnSaveClickListener {
                itemClick.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringListViewHolder {
        return StringListViewHolder(
            ItemGenderListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StringListViewHolder, position: Int) {
        holder.bind(items[position], itemClick)
    }

    override fun getItemCount(): Int = items.size
}