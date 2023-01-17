package com.bestDate.view.alerts

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.bestDate.data.extension.*
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.DialogLikePushBinding
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