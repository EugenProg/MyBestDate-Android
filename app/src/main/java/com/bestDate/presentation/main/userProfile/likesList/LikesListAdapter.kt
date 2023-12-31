package com.bestDate.presentation.main.userProfile.likesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.Like
import com.bestDate.databinding.ItemLikesListBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bumptech.glide.Glide

class LikesListAdapter :
    PagingDataAdapter<Like, LikesListAdapter.LikeListViewHolder>(LikesDiffUtils()) {

    var itemClick: ((Like) -> Unit)? = null

    class LikesDiffUtils : DiffUtil.ItemCallback<Like>() {
        override fun areItemsTheSame(oldItem: Like, newItem: Like): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Like, newItem: Like): Boolean {
            return oldItem == newItem
        }
    }

    class LikeListViewHolder(override val binding: ItemLikesListBinding) :
        BaseClickViewHolder<Like?, ((Like) -> Unit)?, ItemLikesListBinding>(binding) {
        override fun bindView(item: Like?, itemClick: ((Like) -> Unit)?) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item?.user?.main_photo?.thumb_url)
                    .circleCrop()
                    .into(userAvatar)
                Glide.with(itemView.context).load(item?.photo?.thumb_url).centerCrop().into(likedImage)
                online.isVisible = item?.user?.is_online == true
                userName.text = item?.user?.name
                timeView.text = item?.getLikeTime()

                root.setOnSaveClickListener {
                    item?.let {
                        itemClick?.invoke(it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeListViewHolder {
        return LikeListViewHolder(
            ItemLikesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LikeListViewHolder, position: Int) {
        holder.bindView(getItem(position), itemClick)
    }
}