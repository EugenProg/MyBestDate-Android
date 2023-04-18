package com.bestDate.view.bottomSheet.imageSheet

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemImageBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bumptech.glide.Glide

class ImageSheetAdapter(private val imageClick: (Uri) -> Unit) :
    ListAdapter<Uri, ImageSheetAdapter.ImageSheetViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }
    }

    class ImageSheetViewHolder(override val binding: ItemImageBinding) :
        BaseClickViewHolder<Uri, (Uri) -> Unit, ItemImageBinding>(binding) {

        override fun bindView(item: Uri, itemClick: (Uri) -> Unit) {
            Glide.with(itemView.context).load(item).into(binding.image)

            itemView.setOnSaveClickListener { itemClick.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSheetViewHolder {
        return ImageSheetViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageSheetViewHolder, position: Int) {
        holder.bind(getItem(position), imageClick)
    }
}