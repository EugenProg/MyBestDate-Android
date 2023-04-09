package com.bestDate.presentation.main.userProfile.invitationList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.InvitationCard
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemAnsweredInvitationBinding
import com.bestDate.presentation.base.InvitationBaseViewHolder
import com.bumptech.glide.Glide

class AnsweredInvitationsAdapter : BaseInvitationAdapter() {

    class AnsweredInvitationViewHolder(
        override var binding: ItemAnsweredInvitationBinding,
        var userClick: ((ShortUserData?) -> Unit)?
    ) : InvitationBaseViewHolder<ItemAnsweredInvitationBinding>(binding) {
        override fun bind(item: InvitationCard) {
            super.bind(item)
            with(binding) {
                invitationTitle.text = item.invitation?.name

                Glide.with(itemView.context)
                    .load(item.from_user?.getMainPhoto()?.thumb_url)
                    .circleCrop()
                    .into(userInfoView.avatar)
                userInfoView.name.text = item.from_user?.name
                userInfoView.verifyView.isVerified = item.from_user?.full_questionnaire
                userInfoView.location.text = item.from_user?.getLocation()

                val userAnswer = item.getAnswer()
                answer.text = itemView.context.getString(userAnswer.title)
                answerImage.setImageResource(userAnswer.button)

                userInfoView.root.setOnSaveClickListener {
                    userClick?.invoke(item.from_user)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnsweredInvitationViewHolder {
        return AnsweredInvitationViewHolder(
            ItemAnsweredInvitationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            userClick
        )
    }
}