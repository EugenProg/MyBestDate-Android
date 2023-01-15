package com.bestDate.view.bottomSheet.genderSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemBottomSheetListBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bestDate.presentation.registration.GenderType

class GenderSheetAdapter(
    private val items: MutableList<GenderType>,
    private val itemClick: (GenderType) -> Unit
) :
    RecyclerView.Adapter<GenderSheetAdapter.GenderSheetViewHolder>() {

    class GenderSheetViewHolder(override val binding: ItemBottomSheetListBinding) :
        BaseClickViewHolder<GenderType, (GenderType) -> Unit, ItemBottomSheetListBinding>(binding) {

        override fun bindView(item: GenderType, itemClick: (GenderType) -> Unit) {
            binding.name.text = itemView.context.getString(item.line)

            itemView.setOnSaveClickListener {
                itemClick.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderSheetViewHolder {
        return GenderSheetViewHolder(
            ItemBottomSheetListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GenderSheetViewHolder, position: Int) {
        holder.bind(items[position], itemClick)
    }

    override fun getItemCount(): Int = items.size
}