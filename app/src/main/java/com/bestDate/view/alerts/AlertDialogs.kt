package com.bestDate.view.alerts

import android.view.Gravity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.model.Match
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.DialogDefaultBinding
import com.bestDate.databinding.DialogMatchActionBinding
import com.bumptech.glide.Glide

fun FragmentActivity.showDefaultDialog(message: String) {
    val binding = DialogDefaultBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, Gravity.BOTTOM)

    binding.message.text = message
    postDelayed({
        dialog.dismiss()
    }, 2200)
}

fun FragmentActivity.showMatchActionDialog(
    match: Match,
    mainPhoto: String,
    openProfile: (ShortUserData?) -> Unit,
    openChat: (ShortUserData?) -> Unit
) {
    val binding = DialogMatchActionBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root)

    with(binding) {
        box.showWithSlideTopAnimation {
            Glide.with(this@showMatchActionDialog)
                .load(match.user?.getMainPhoto()?.thumb_url)
                .circleCrop()
                .into(avatar)
            Glide.with(this@showMatchActionDialog)
                .load(match.user?.getMainPhoto()?.thumb_url)
                .into(matchPhoto)
            Glide.with(this@showMatchActionDialog)
                .load(mainPhoto)
                .into(myPhoto)

            name.text = match.user?.name
            location.text = match.user?.getLocation()
        }

        avatarBox.setOnSaveClickListener {
            dialog.dismiss()
            openProfile.invoke(match.user)
        }
        matchesBox.setOnSaveClickListener {
            dialog.dismiss()
            openProfile.invoke(match.user)
        }
        writeMessageBtn.setOnSaveClickListener {
            dialog.dismiss()
            openChat.invoke(match.user)
        }
        closeBtn.setOnSaveClickListener {
            dialog.closeWithAnimation(root, owner = this@showMatchActionDialog)
        }

        postDelayed({
            animationView.setAnimation(R.raw.match_action_anim)
            animationView.playAnimation()
        }, 300)
        postDelayed({
            animationView.isVisible = false
        }, 2700)
    }
}