package com.bestDate.presentation.main.userProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bestDate.R
import com.bestDate.base.BaseClickViewHolder
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.ItemAddImageBinding
import com.bestDate.databinding.ItemImageListBinding
import com.bumptech.glide.Glide

class ImageLineAdapter: ListAdapter<ProfileImage, ViewHolder>(ImageListDiffUtils()) {

    var imageClick: ((ProfileImage) -> Unit)? = null
    var addClick: (() -> Unit)? = null

    class ImageListDiffUtils: DiffUtil.ItemCallback<ProfileImage>() {
        override fun areItemsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
            return oldItem == newItem
        }
    }

    class ImageListViewHolder(override val binding: ItemImageListBinding):
        BaseClickViewHolder<ProfileImage, ((ProfileImage) -> Unit)?, ItemImageListBinding>(binding) {

        override fun bindView(item: ProfileImage, itemClick: ((ProfileImage) -> Unit)?) {
            itemView.apply {
                Glide.with(itemView.context)
                    .load(item.thumb_url)
                    .circleCrop()
                    .into(binding.image)

                binding.top.isVisible = item.top == true
            }
        }
    }

    class AddImageViewHolder(override val binding: ItemAddImageBinding):
        BaseClickViewHolder<ProfileImage, (() -> Unit)?, ItemAddImageBinding>(binding) {
        override fun bindView(item: ProfileImage, itemClick: (() -> Unit)?) {
            itemView.apply {
                setOnSaveClickListener { itemClick?.invoke() }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).id == -1) R.layout.item_add_image
        else R.layout.item_image_list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == R.layout.item_add_image) {
            AddImageViewHolder(ItemAddImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            ImageListViewHolder(ItemImageListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is AddImageViewHolder) holder.bind(getItem(position), addClick)
        if (holder is ImageListViewHolder) holder.bind(getItem(position), imageClick)
    }
}