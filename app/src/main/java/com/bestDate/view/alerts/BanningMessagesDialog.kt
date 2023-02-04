package com.bestDate.view.alerts

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.bestDate.data.extension.*
import com.bestDate.databinding.DialogBanningMessagesBinding

fun FragmentActivity.showBanningMessagesDialog(navigateAction: (() -> Unit)) {
    val binding = DialogBanningMessagesBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.CENTER)

    with(binding) {
        box.showWithSlideTopAnimation {}

        closeBtn.setOnSaveClickListener {
            dialog.closeWithAnimation(root, this@showBanningMessagesDialog)
        }

        choosePlanBtn.onClick = {
            dialog.closeWithAnimation(root, this@showBanningMessagesDialog)
            navigateAction.invoke()
        }
    }
}
