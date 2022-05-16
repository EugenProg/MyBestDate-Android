package com.bestDate.fragment.profilePhotoEditor

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.PageImagesListBinding
import com.bumptech.glide.Glide

class ImagesPageAdapter(private val imageList: MutableList<Uri>,
                        private val imageClick: (Uri) -> Unit):
    RecyclerView.Adapter<ImagesPageAdapter.ImagesPageViewHolder>() {

    class ImagesPageViewHolder(val binding: PageImagesListBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(items: MutableList<Uri>, imageClick: (Uri) -> Unit) {
            itemView.apply {
                with(binding) {
                    when (items.size) {
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
                        3 -> {
                            setImagesToView(context, firstImage, items[0], imageClick)
                            setImagesToView(context, secondImage, items[1], imageClick)
                            setImagesToView(context, thirdImage, items[2], imageClick)
                        }
                    }
                }
            }
        }

        private fun setImagesToView(context: Context, view: ImageView, image: Uri?, setClick: (Uri) -> Unit) {
            if (image != null) {
                Glide.with(context).load(image).into(view)
                view.setOnSaveClickListener { setClick.invoke(image) }
            }
            else view.setImageResource(R.color.white_10)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesPageViewHolder {
        return ImagesPageViewHolder(
            PageImagesListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImagesPageViewHolder, position: Int) {
        holder.onBind(getPageImages(position), imageClick)
    }

    private fun getPageImages(position: Int): MutableList<Uri> {
        val images: MutableList<Uri> = ArrayList()

        for (i in 0..2) {
            val index = (position * 3) + i
            if (index < imageList.size) images.add(imageList[index])
        }

        return images
    }

    override fun getItemCount(): Int = ((imageList.size - 1) / 3) + 1
}