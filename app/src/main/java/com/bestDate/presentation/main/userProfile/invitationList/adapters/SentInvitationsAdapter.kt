package com.bestDate.presentation.main.userProfile.invitationList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.InvitationAnswer
import com.bestDate.data.model.InvitationCard
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemSentInvitationBinding
import com.bestDate.presentation.base.InvitationBaseViewHolder
import com.bumptech.glide.Glide

class SentInvitationsAdapter : BaseInvitationAdapter() {

    class SentInvitationViewHolder(
        override var binding: ItemSentInvitationBinding,
        var userClick: ((ShortUserData?) -> Unit)?
    ) : InvitationBaseViewHolder<ItemSentInvitationBinding>(binding) {
        override fun bind(item: InvitationCard) {
            super.bind(item)
            with(binding) {
                invitationTitle.text = item.invitation?.name

                Glide.with(itemView.context)
                    .load(item.to_user?.getMainPhoto()?.thumb_url)
                    .circleCrop()
                    .into(userInfoView.avatar)
                userInfoView.name.text = item.to_user?.name
                userInfoView.verifyView.isVerified = item.to_user?.full_questionnaire
                userInfoView.location.text = item.to_user?.getLocation()

                val userAnswer = item.getAnswer()
                if (userAnswer != InvitationAnswer.NONE) {
                    answer.text = itemView.context.getString(userAnswer.title)
                    answerImage.isVisible = true
                    answerImage.setImageResource(userAnswer.button)
                } else {
                    answer.text = itemView.context.getString(
                        R.string.user_has_not_given_answer_yet,
                        item.to_user?.name
                    )
                    answerImage.isVisible = false
                }

                userInfoView.root.setOnSaveClickListener {
                    userClick?.invoke(item.to_user)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SentInvitationViewHolder {
        return SentInvitationViewHolder(
            ItemSentInvitationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), userClick
        )
    }
}