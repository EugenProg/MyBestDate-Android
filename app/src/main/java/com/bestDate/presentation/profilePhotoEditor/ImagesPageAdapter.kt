package com.bestDate.presentation.profilePhotoEditor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.Image
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.PageImagesListBinding
import com.bumptech.glide.Glide

class ImagesPageAdapter(private val imageClick: (ProfileImage) -> Unit):
    ListAdapter<MutableList<ProfileImage>, ImagesPageAdapter.ImagesPageViewHolder>(DiffCallback()) {

    private class DiffCallback: DiffUtil.ItemCallback<MutableList<ProfileImage>>() {
        override fun areItemsTheSame(oldItem: MutableList<ProfileImage>, newItem: MutableList<ProfileImage>): Boolean {
            if (oldItem.size != newItem.size) return false

            for (index in oldItem.indices) {
                if (oldItem[index].id != newItem[index].id) return false
            }
            return true
        }

        override fun areContentsTheSame(oldItem: MutableList<ProfileImage>, newItem: MutableList<ProfileImage>): Boolean {
            if (oldItem.size != newItem.size) return false

            for (index in oldItem.indices) {
                if (oldItem[index] != newItem[index]) return false
            }
            return true
        }
    }

    class ImagesPageViewHolder(val binding: PageImagesListBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(items: MutableList<ProfileImage>, imageClick: (ProfileImage) -> Unit) {
            itemView.apply {
                with(binding) {
                    when (items.size) {
                        0 -> {
                            setImagesToView(context, firstImage, null, imageClick)
                            setImagesToView(context, secondImage, null, imageClick)
                            setImagesToView(context, thirdImage, null, imageClick)
                        }
                        1 -> {
                            setImagesToView(context, firstImage, items[0], imageClick)
                            setImagesToView(context, secondImage, null, imageClick)
                            setImagesToView(context, thirdImage, null, imageClick)
                        }
                        2 -> {
                            setImagesToView(context, firstImage, items[0], imageClick)
                            setImagesToView(context, secondImage, items[1], imageClick)
                            setImagesToView(context, thirdImage, null, imageClick)
                        }
                        else -> {
                            setImagesToView(context, firstImage, items[0], imageClick)
                            setImagesToView(context, secondImage, items[1], imageClick)
                            setImagesToView(context, thirdImage, items[2], imageClick)
                        }
                    }
                }
            }
        }

        private fun setImagesToView(context: Context, view: ImageView, image: ProfileImage?, setClick: (ProfileImage) -> Unit) {
            if (image != null) {
                Glide.with(context).load(image.thumb_url).into(view)
                view.setOnSaveClickListener {
                    setClick.invoke(image)
                }
            }
            else {
                view.setImageResource(R.color.white_10)
                view.setOnClickListener{ }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesPageViewHolder {
        return ImagesPageViewHolder(
            PageImagesListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImagesPageViewHolder, position: Int) {
        holder.onBind(getItem(position), imageClick)
    }
}