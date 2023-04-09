package com.bestDate.presentation.main.userProfile.invitationList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.data.extension.rotateHorizontally
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.InvitationAnswer
import com.bestDate.data.model.InvitationCard
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemNewInvitationBinding
import com.bestDate.presentation.base.InvitationBaseViewHolder
import com.bumptech.glide.Glide

class NewInvitationsAdapter : BaseInvitationAdapter() {

    class NewInvitationViewHolder(
        override var binding: ItemNewInvitationBinding,
        var answerClick: ((InvitationAnswer, Int?) -> Unit)?,
        var userClick: ((ShortUserData?) -> Unit)?
    ) : InvitationBaseViewHolder<ItemNewInvitationBinding>(binding) {
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

                userInfoView.root.setOnSaveClickListener {
                    userClick?.invoke(item.from_user)
                }
                yesButton.setOnSaveClickListener {
                    answerClick?.invoke(InvitationAnswer.YES, item.id)
                }
                yesNextButton.setOnSaveClickListener {
                    answerClick?.invoke(InvitationAnswer.YES_NEXT_TIME, item.id)
                }
                thankNoButton.setOnSaveClickListener {
                    answerClick?.invoke(InvitationAnswer.NOT_YET, item.id)
                }
                noButton.setOnSaveClickListener {
                    answerClick?.invoke(InvitationAnswer.NO, item.id)
                }

                itemView.setOnSaveClickListener {
                    val rotationDegree = if (frontSide.isVisible) 180f else 0f
                    binding.root.rotateHorizontally(degree = rotationDegree) {
                        frontSide.isVisible = !frontSide.isVisible
                        backSide.isVisible = !backSide.isVisible
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationBaseViewHolder<*> {
        return NewInvitationViewHolder(
            ItemNewInvitationBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            answerClick, userClick
        )
    }
}