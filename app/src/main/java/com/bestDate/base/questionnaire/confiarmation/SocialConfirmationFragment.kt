package com.bestDate.base.questionnaire.confiarmation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.base.questionnaire.QuestionnaireQuestion
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.utils.SocialTypes
import com.bestDate.data.utils.SocialUtils
import com.bestDate.databinding.FragmentSocialConfirmationBinding

class SocialConfirmationFragment(private val question: QuestionnaireQuestion) :
    BaseFragment<FragmentSocialConfirmationBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSocialConfirmationBinding =
        { inflater, parent, attach ->
            FragmentSocialConfirmationBinding.inflate(
                inflater,
                parent,
                attach
            )
        }

    override val statusBarLight = true
    override val navBarLight = true
    override val navBarColor = R.color.white

    var backClick: (() -> Unit)? = null
    var saveClick: ((String) -> Unit)? = null
    override var customBackNavigation = true
    var socialUtils = SocialUtils()

    override fun onInit() {
        super.onInit()
        socialUtils.setSocials(question.answer)

        with(binding) {
            instaInput.setText(socialUtils.getLink(SocialTypes.INSTAGRAM))
            facebookInput.setText(socialUtils.getLink(SocialTypes.FACEBOOK))
            twitterInput.setText(socialUtils.getLink(SocialTypes.TWITTER))
            linkedinInput.setText(socialUtils.getLink(SocialTypes.LINKEDIN))

            instaInput.setFocus()
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = {
                goBack()
            }
            saveButton.setOnSaveClickListener {
                saveClick?.invoke(socialUtils.getSocials())
            }
            instaInput.onTextChangeListener = {
                instaInput.isChecked(socialUtils.isValidLink(it, SocialTypes.INSTAGRAM))
                socialUtils.setLink(it, SocialTypes.INSTAGRAM)
            }
            facebookInput.onTextChangeListener = {
                facebookInput.isChecked(socialUtils.isValidLink(it, SocialTypes.FACEBOOK))
                socialUtils.setLink(it, SocialTypes.FACEBOOK)
            }
            twitterInput.onTextChangeListener = {
                twitterInput.isChecked(socialUtils.isValidLink(it, SocialTypes.TWITTER))
                socialUtils.setLink(it, SocialTypes.TWITTER)
            }
            linkedinInput.onTextChangeListener = {
                linkedinInput.isChecked(socialUtils.isValidLink(it, SocialTypes.LINKEDIN))
                socialUtils.setLink(it, SocialTypes.LINKEDIN)
            }
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        with(binding) {
            var focusView = instaInput
            if (facebookInput.hasFocus()) focusView = facebookInput
            if (twitterInput.hasFocus()) focusView = twitterInput
            if (linkedinInput.hasFocus()) focusView = linkedinInput

            scroll.fullScroll(View.FOCUS_DOWN)
            focusView.setFocus()
        }
    }

    override fun goBack() {
        backClick?.invoke()
    }
}