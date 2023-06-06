package com.bestDate.view.alerts

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import com.bestDate.data.extension.getDialog
import com.bestDate.databinding.DialogLostConnectionBinding

class LostConnectionDialog(val activity: Activity) {
    private val binding: DialogLostConnectionBinding =
        DialogLostConnectionBinding.inflate(activity.layoutInflater)
    private var dialog: Dialog? = null

    fun startLoading() {
        if (dialog == null) {
            dialog = getDialog(binding.root, Gravity.TOP)
            dialog?.setCancelable(false)
        } else {
            dialog?.show()
        }
    }

    fun stopLoading() {
        dialog?.dismiss()
    }
}
