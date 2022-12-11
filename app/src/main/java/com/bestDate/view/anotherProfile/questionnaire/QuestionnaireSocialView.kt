package com.bestDate.view.anotherProfile.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bestDate.data.extension.openWebAddress
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ViewQuestionnaireSocialBinding

class QuestionnaireSocialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewQuestionnaireSocialBinding = ViewQuestionnaireSocialBinding.inflate(
        LayoutInflater.from(context), this)

    private var socialsMap: MutableMap<SocialTypes, String> = mutableMapOf()

    init {
        with(binding) {
            insta.setOnSaveClickListener {
                context.openWebAddress(socialsMap[SocialTypes.INSTAGRAM])
            }
            facebook.setOnSaveClickListener {
                context.openWebAddress(socialsMap[SocialTypes.FACEBOOK])
            }
            twitter.setOnSaveClickListener {
                context.openWebAddress(socialsMap[SocialTypes.TWITTER])
            }
            linkedin.setOnSaveClickListener {
                context.openWebAddress(socialsMap[SocialTypes.LINKEDIN])
            }
        }
    }

    fun setSocials(socials: MutableList<String>?) {
        binding.root.isVisible = !socials.isNullOrEmpty()

        createSocialMap(socials)

        with(binding) {
            insta.isVisible = socialsMap.containsKey(SocialTypes.INSTAGRAM)
            facebook.isVisible = socialsMap.containsKey(SocialTypes.FACEBOOK)
            twitter.isVisible = socialsMap.containsKey(SocialTypes.TWITTER)
            linkedin.isVisible = socialsMap.containsKey(SocialTypes.LINKEDIN)
        }
    }

    private fun createSocialMap(socials: MutableList<String>?) {
        socials?.forEach {
            when {
                it.contains(SocialTypes.INSTAGRAM.baseUrl) -> socialsMap[SocialTypes.INSTAGRAM] = it
                it.contains(SocialTypes.FACEBOOK.baseUrl) -> socialsMap[SocialTypes.FACEBOOK] = it
                it.contains(SocialTypes.TWITTER.baseUrl) -> socialsMap[SocialTypes.TWITTER] = it
                it.contains(SocialTypes.LINKEDIN.baseUrl) -> socialsMap[SocialTypes.LINKEDIN] = it
            }
        }
    }
}

enum class SocialTypes(val baseUrl: String) {
    INSTAGRAM("instagram.com"),
    FACEBOOK("facebook.com"),
    TWITTER("twitter.com"),
    LINKEDIN("linkedin.com")
}