package com.bestDate.presentation.main.userProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.setWidth
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.ItemAddImageBinding
import com.bestDate.databinding.ItemImageListBinding
import com.bestDate.databinding.ItemImagePreviewBinding
import com.bestDate.presentation.base.ImageLineBaseViewHolder
import com.bumptech.glide.Glide

class ImageLineAdapter(var viewHeight: Int, var showTop: Boolean) :
    ListAdapter<ProfileImage, ImageLineBaseViewHolder<*, *>>(ImageListDiffUtils()) {

    var imageClick: ((ProfileImage) -> Unit)? = null
    var addClick: (() -> Unit)? = null

    class ImageListDiffUtils : DiffUtil.ItemCallback<ProfileImage>() {
        override fun areItemsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
            return oldItem == newItem
        }
    }

    class ImageListViewHolder(
        override val binding: ItemImageListBinding,
        private val viewHeight: Int, private val showTop: Boolean
    ) : ImageLineBaseViewHolder<ItemImageListBinding, ((ProfileImage) -> Unit)?>(binding) {

        override fun bindView(item: ProfileImage, itemClick: ((ProfileImage) -> Unit)?) {
            itemView.apply {
                itemView.setWidth(viewHeight)
                Glide.with(itemView.context)
                    .load(item.thumb_url)
                    .centerCrop()
                    .into(binding.image)

                binding.top.isVisible = item.top == true && showTop
                binding.moderatedBox.isVisible = item.moderated == true

                setOnSaveClickListener {
                    if (item.moderated == false) {
                        itemClick?.invoke(item)
                    }
                }
            }
        }
    }

    class AddImageViewHolder(
        override val binding: ItemAddImageBinding,
        private val viewHeight: Int
    ) : ImageLineBaseViewHolder<ItemAddImageBinding, (() -> Unit)?>(binding) {
        override fun bindView(item: ProfileImage, itemClick: (() -> Unit)?) {
            itemView.apply {
                itemView.setWidth(viewHeight)
                setOnSaveClickListener { itemClick?.invoke() }
            }
        }
    }

    class ImagePreviewHolder(
        override val binding: ItemImagePreviewBinding,
        private val viewHeight: Int
    ) :
        ImageLineBaseViewHolder<ItemImagePreviewBinding, (() -> Unit)?>(binding) {
        override fun bindView(item: ProfileImage, itemClick: (() -> Unit)?) {
            itemView.setWidth(viewHeight)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.ordinal
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageLineBaseViewHolder<*, *> {
        return when (viewType) {
            ProfileImage.ViewType.ADD.ordinal -> AddImageViewHolder(
                ItemAddImageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), viewHeight
            )

            ProfileImage.ViewType.PREVIEW.ordinal -> ImagePreviewHolder(
                ItemImagePreviewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), viewHeight
            )

            else -> ImageListViewHolder(
                ItemImageListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                viewHeight, showTop
            )
        }
    }

    override fun onBindViewHolder(holder: ImageLineBaseViewHolder<*, *>, position: Int) {
        when (holder) {
            is AddImageViewHolder -> holder.bind(getItem(position), addClick)
            is ImageListViewHolder -> holder.bind(getItem(position), imageClick)
            is ImagePreviewHolder -> holder.bind(getItem(position))
        }
    }
}