package com.bestDate.view.alerts

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.bestDate.data.extension.*
import com.bestDate.databinding.DialogBanningMessagesBinding

fun FragmentActivity.showBanningMessagesDialog(navigateAction: (() -> Unit)) {
    val binding = DialogBanningMessagesBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.TOP)

    with(binding) {
        box.showWithSlideBottomAnimation {}

        closeBtn.setOnSaveClickListener {
            dialog.closeWithSlideTopAnimation(root, this@showBanningMessagesDialog)
        }

        root.setOnSaveClickListener {
            dialog.closeWithSlideTopAnimation(root, this@showBanningMessagesDialog)
            navigateAction.invoke()
        }

        postDelayed({
            dialog.closeWithSlideTopAnimation(root, this@showBanningMessagesDialog)
        }, 3000)
    }
}
