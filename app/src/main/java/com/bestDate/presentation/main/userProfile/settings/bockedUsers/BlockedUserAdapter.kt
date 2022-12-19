package com.bestDate.presentation.main.userProfile.settings.bockedUsers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.base.BaseViewHolder
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemBlockedUserBinding
import com.bumptech.glide.Glide

class BlockedUserAdapter :
    ListAdapter<ShortUserData, BlockedUserAdapter.BlockedUsersViewHolder>(BlockedUsersDiffUtil()) {

    var openClick: ((ShortUserData?) -> Unit)? = null
    var unlockClick: ((Int?) -> Unit)? = null

    class BlockedUsersDiffUtil : DiffUtil.ItemCallback<ShortUserData>() {
        override fun areItemsTheSame(oldItem: ShortUserData, newItem: ShortUserData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShortUserData, newItem: ShortUserData): Boolean {
            return oldItem == newItem
        }
    }

    class BlockedUsersViewHolder(
        override val binding: ItemBlockedUserBinding,
        var openClick: ((ShortUserData?) -> Unit)?,
        var unlockClick: ((Int?) -> Unit)?
    ) :
        BaseViewHolder<ShortUserData, ItemBlockedUserBinding>(binding) {
        override fun bind(item: ShortUserData) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.getMainPhoto().thumb_url)
                    .circleCrop()
                    .into(userAvatar)
                online.isVisible = item.is_online == true
                userName.text = item.name

                root.setOnSaveClickListener {
                    openClick?.invoke(item)
                }

                unblockButton.setOnSaveClickListener {
                    unlockClick?.invoke(item.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedUsersViewHolder {
        return BlockedUsersViewHolder(
            ItemBlockedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            openClick, unlockClick
        )
    }

    override fun onBindViewHolder(holder: BlockedUsersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}