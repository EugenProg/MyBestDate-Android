package com.bestDate.view.alerts

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.bestDate.data.extension.*
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.DialogDefaultPushBinding
import com.bestDate.databinding.DialogInvitationPushBinding
import com.bestDate.databinding.DialogLikePushBinding
import com.bestDate.databinding.DialogMatchPushBinding
import com.bumptech.glide.Glide

fun FragmentActivity.showLikePush(userData: ShortUserData?, navigateAction: (() -> Unit)) {
    val binding = DialogLikePushBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.TOP)

    with(binding) {
        box.showWithSlideBottomAnimation {
            Glide.with(this@showLikePush)
                .load(userData?.getMainPhoto()?.thumb_url)
                .circleCrop()
                .into(avatar)
            name.text = userData?.name
        }

        closeBtn.setOnSaveClickListener {
            dialog.closeWithSlideTopAnimation(root, this@showLikePush)
        }

        root.setOnSaveClickListener {
            dialog.closeWithSlideTopAnimation(root, this@showLikePush)
            navigateAction.invoke()
        }

        postDelayed({
            dialog.closeWithSlideTopAnimation(root, this@showLikePush)
        }, 3000)
    }
}

fun FragmentActivity.showMatchPush(userData: ShortUserData?, navigateAction: (() -> Unit)) {
    val binding = DialogMatchPushBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.TOP)

    with(binding) {
        box.showWithSlideBottomAnimation {
            Glide.with(this@showMatchPush)
                .load(userData?.getMainPhoto()?.thumb_url)
                .circleCrop()
                .into(avatar)
            name.text = userData?.name
            location.text = userData?.getLocation()
        }

        closeBtn.setOnSaveClickListener {
            dialog.closeWithSlideTopAnimation(root, this@showMatchPush)
        }

        root.setOnSaveClickListener {
            dialog.closeWithSlideTopAnimation(root, this@showMatchPush)
            navigateAction.invoke()
        }

        postDelayed({
            dialog.closeWithSlideTopAnimation(root, this@showMatchPush)
        }, 3000)
    }
}

fun FragmentActivity.showInvitationPush(userData: ShortUserData?, navigateAction: (() -> Unit)) {
    val binding = DialogInvitationPushBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.TOP)

    with(binding) {
        box.showWithSlideBottomAnimation {
            Glide.with(this@showInvitationPush)
                .load(userData?.getMainPhoto()?.thumb_url)
                .circleCrop()
                .into(avatar)
            name.text = userData?.name
            location.text = userData?.getLocation()
        }

        goToButton.setOnSaveClickListener {
            dialog.closeWithSlideTopAnimation(root, this@showInvitationPush)
            navigateAction.invoke()
        }

        root.setOnSaveClickListener {
            dialog.closeWithSlideTopAnimation(root, this@showInvitationPush)
            navigateAction.invoke()
        }

        postDelayed({
            dialog.closeWithSlideTopAnimation(root, this@showInvitationPush)
        }, 3000)
    }
}

fun FragmentActivity.showDefaultPush(title: String?, body: String?, navigateAction: (() -> Unit)) {
    val binding = DialogDefaultPushBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.TOP)

    with(binding) {
        box.showWithSlideBottomAnimation {
            titleView.text = title
            bodyView.text = body
        }

        root.setOnSaveClickListener {
            dialog.closeWithSlideTopAnimation(root, this@showDefaultPush)
            navigateAction.invoke()
        }

        postDelayed({
            dialog.closeWithSlideTopAnimation(root, this@showDefaultPush)
        }, 3000)
    }
}