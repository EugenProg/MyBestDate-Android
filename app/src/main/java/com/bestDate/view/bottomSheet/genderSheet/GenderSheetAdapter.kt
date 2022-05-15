package com.bestDate.view.bottomSheet.genderSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.GenderListItemBinding

class GenderSheetAdapter(private val items: MutableList<String>,
                         private val itemClick: (String) -> Unit):
    RecyclerView.Adapter<GenderSheetAdapter.GenderSheetViewHolder>() {

    class GenderSheetViewHolder(val binding: GenderListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: String, itemClick: (String) -> Unit) {
            itemView.apply {
                binding.name.text = item
            }

            itemView.setOnSaveClickListener {
                itemClick.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderSheetViewHolder {
        return GenderSheetViewHolder(
            GenderListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenderSheetViewHolder, position: Int) {
        holder.onBind(items[position], itemClick)
    }

    override fun getItemCount(): Int = items.size
}