package com.bestDate.view.anotherProfile.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bestDate.data.extension.openWebAddress
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.utils.SocialTypes
import com.bestDate.data.utils.SocialUtils
import com.bestDate.databinding.ViewQuestionnaireSocialBinding

class QuestionnaireSocialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewQuestionnaireSocialBinding = ViewQuestionnaireSocialBinding.inflate(
        LayoutInflater.from(context), this
    )

    private var socialUtils = SocialUtils()

    init {
        with(binding) {
            insta.setOnSaveClickListener {
                context.openWebAddress(socialUtils.getLink(SocialTypes.INSTAGRAM))
            }
            facebook.setOnSaveClickListener {
                context.openWebAddress(socialUtils.getLink(SocialTypes.FACEBOOK))
            }
            twitter.setOnSaveClickListener {
                context.openWebAddress(socialUtils.getLink(SocialTypes.TWITTER))
            }
            linkedin.setOnSaveClickListener {
                context.openWebAddress(socialUtils.getLink(SocialTypes.LINKEDIN))
            }
        }
    }

    fun setSocials(socials: MutableList<String>?) {
        binding.root.isVisible = !socials.isNullOrEmpty()

        socialUtils.setSocials(socials)

        with(binding) {
            insta.isVisible = socialUtils.instaAvailable()
            facebook.isVisible = socialUtils.facebookAvailable()
            twitter.isVisible = socialUtils.twitterAvailable()
            linkedin.isVisible = socialUtils.linkedinAvailable()
        }
    }
}