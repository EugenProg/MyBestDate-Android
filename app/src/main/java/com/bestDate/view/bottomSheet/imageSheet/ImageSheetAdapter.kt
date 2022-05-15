package com.bestDate.view.bottomSheet.imageSheet

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ImageItemBinding
import com.bumptech.glide.Glide

class ImageSheetAdapter(private val imageClick: (Uri) -> Unit):
    ListAdapter<Uri, ImageSheetAdapter.ImageSheetViewHolder>(DiffCallback()) {

    private class DiffCallback: DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }
    }

    class ImageSheetViewHolder(val binding: ImageItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Uri, imageClick: (Uri) -> Unit) {
            itemView.apply {
                Glide.with(context).load(item).into(binding.image)
            }

            itemView.setOnSaveClickListener { imageClick.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSheetViewHolder {
        return ImageSheetViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageSheetViewHolder, position: Int) {
        holder.onBind(getItem(position), imageClick)
    }
}