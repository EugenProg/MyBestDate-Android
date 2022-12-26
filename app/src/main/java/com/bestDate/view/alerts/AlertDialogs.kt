package com.bestDate.view.alerts

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.bestDate.data.extension.closeWithAnimation
import com.bestDate.data.extension.getDialog
import com.bestDate.data.extension.postDelayed
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.DialogDefaultBinding
import com.bestDate.databinding.DialogDeleteProfileBinding
import com.bestDate.databinding.DialogLoadingBinding

fun FragmentActivity.showDefaultDialog(message: String) {
    val binding = DialogDefaultBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, Gravity.BOTTOM)

    binding.message.text = message
    postDelayed({
        dialog.dismiss()
    }, 2200)
}

class LoaderDialog(val activity: Activity) {
    private val binding: DialogLoadingBinding =
        DialogLoadingBinding.inflate(activity.layoutInflater)
    private var dialog: Dialog? = null

    fun startLoading() {
        if (dialog == null) {
            dialog = getDialog(binding.root)
        } else {
            dialog?.show()
        }
    }

    fun stopLoading() {
        dialog?.dismiss()
    }
}

fun FragmentActivity.showDeleteProfileDialog(deleteCLick: (() -> Unit)) {
    val binding = DialogDeleteProfileBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root)

    binding.deleteButton.setOnSaveClickListener {
        deleteCLick.invoke()
        dialog.closeWithAnimation(binding.root, this)
    }
    binding.cancelButton.setOnSaveClickListener {
        dialog.closeWithAnimation(binding.root, this)
    }
}