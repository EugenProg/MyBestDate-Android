package com.bestDate.view.photoSlider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.data.extension.onSwipeListener
import com.bestDate.presentation.base.BaseViewHolder
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.ItemPhotoSliderBinding
import com.bumptech.glide.Glide

class PhotoSliderAdapter:
    ListAdapter<ProfileImage, PhotoSliderAdapter.PhotoSliderViewHolder>(PhotoSliderDiffUtil()) {

    var onSwipe: (() -> Unit)? = null

    class PhotoSliderDiffUtil: DiffUtil.ItemCallback<ProfileImage>() {
        override fun areItemsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
            return oldItem == newItem
        }
    }

    class PhotoSliderViewHolder(override val binding: ItemPhotoSliderBinding,
                                var onSwipe: (() -> Unit)?):
        BaseViewHolder<ProfileImage?, ItemPhotoSliderBinding>(binding) {

        override fun bind(item: ProfileImage?) {
            Glide.with(itemView.context)
                .load(item?.thumb_url)
                .centerCrop()
                .into(binding.photo)

            Glide.with(itemView.context)
                .load(item?.full_url)
                .centerCrop()
                .into(binding.photo)

            binding.root.onSwipeListener(onSwipeBottom = {
                onSwipe?.invoke()
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoSliderViewHolder {
        return PhotoSliderViewHolder(
            ItemPhotoSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onSwipe
        )
    }

    override fun onBindViewHolder(holder: PhotoSliderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}