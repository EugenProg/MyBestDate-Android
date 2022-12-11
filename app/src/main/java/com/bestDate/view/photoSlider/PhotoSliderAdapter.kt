package com.bestDate.view.photoSlider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.base.BaseViewHolder
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.ItemPhotoSliderBinding
import com.bumptech.glide.Glide

class PhotoSliderAdapter:
    ListAdapter<ProfileImage, PhotoSliderAdapter.PhotoSliderViewHolder>(PhotoSliderDiffUtil()) {

    class PhotoSliderDiffUtil: DiffUtil.ItemCallback<ProfileImage>() {
        override fun areItemsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
            return oldItem == newItem
        }
    }

    class PhotoSliderViewHolder(override val binding: ItemPhotoSliderBinding):
        BaseViewHolder<ProfileImage?, ItemPhotoSliderBinding>(binding) {

        override fun bind(item: ProfileImage?) {
            Glide.with(itemView.context)
                .load(item?.full_url)
                .into(binding.photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoSliderViewHolder {
        return PhotoSliderViewHolder(
            ItemPhotoSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhotoSliderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}