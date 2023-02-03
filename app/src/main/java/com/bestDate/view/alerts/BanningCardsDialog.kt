package com.bestDate.view.alerts

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.bestDate.data.extension.*
import com.bestDate.databinding.DialogBanningCardsBinding

fun FragmentActivity.showBanningCardsDialog(navigateAction: (() -> Unit)) {
    val binding = DialogBanningCardsBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.CENTER)

    with(binding) {
        box.showWithSlideTopAnimation {}

        closeBtn.setOnSaveClickListener {
            dialog.closeWithAnimation(root, this@showBanningCardsDialog)
        }

        choosePlanBtn.onClick = {
            dialog.closeWithAnimation(root, this@showBanningCardsDialog)
            navigateAction.invoke()
        }
    }
}