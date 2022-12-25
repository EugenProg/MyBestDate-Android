package com.bestDate.view.questionnaire.list

import androidx.core.view.isVisible
import com.bestDate.base.QuestionnaireBaseViewHolder
import com.bestDate.base.questionnaire.QuestionnaireQuestion
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.utils.SocialUtils
import com.bestDate.databinding.ItemConfirmationSocialBinding

class ConfirmationSocialViewHolder(override val binding: ItemConfirmationSocialBinding) :
    QuestionnaireBaseViewHolder<ItemConfirmationSocialBinding>(binding) {
    override fun bindView(
        item: QuestionnaireQuestion,
        itemClick: ((QuestionnaireQuestion) -> Unit)?
    ) {
        val socialUtils = SocialUtils()
        socialUtils.setSocials(item.answer)
        with(binding) {
            socialContainer.isVisible = !item.answer.isNullOrEmpty()
            confirmedImage.isVerified = !item.answer.isNullOrEmpty()

            insta.isVisible = socialUtils.instaAvailable()
            facebook.isVisible = socialUtils.facebookAvailable()
            twitter.isVisible = socialUtils.twitterAvailable()
            linkedin.isVisible = socialUtils.linkedinAvailable()

            root.setOnSaveClickListener { itemClick?.invoke(item) }
        }
    }
}