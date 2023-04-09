package com.bestDate.presentation.main.userProfile.invitationList.adapters

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bestDate.data.model.InvitationAnswer
import com.bestDate.data.model.InvitationCard
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.base.InvitationBaseViewHolder

abstract class BaseInvitationAdapter :
    PagingDataAdapter<InvitationCard, InvitationBaseViewHolder<*>>(InvitationCardDiffUtil()) {

    var userClick: ((ShortUserData?) -> Unit)? = null
    var answerClick: ((InvitationAnswer, Int?) -> Unit)? = null

    class InvitationCardDiffUtil : DiffUtil.ItemCallback<InvitationCard>() {
        override fun areItemsTheSame(oldItem: InvitationCard, newItem: InvitationCard): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InvitationCard, newItem: InvitationCard): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: InvitationBaseViewHolder<*>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}