package com.bestDate.view.alerts

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.bestDate.data.extension.getDialog
import com.bestDate.data.extension.hideWithScaleAnimation
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.showWithScaleAnimation
import com.bestDate.databinding.DialogSubscriptionSuccessBinding

fun FragmentActivity.showSubscriptionSuccessDialog(closeClick: () -> Unit) {
    val binding = DialogSubscriptionSuccessBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, position = Gravity.CENTER)

    with(binding) {
        box.showWithScaleAnimation { }

        closeBtn.setOnSaveClickListener {
            dialog.hideWithScaleAnimation(root, this@showSubscriptionSuccessDialog) {
                closeClick.invoke()
            }
        }
    }
}