package com.bestDate.view.alerts

import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.bestDate.data.extension.getDialog
import com.bestDate.data.extension.postDelayed
import com.bestDate.databinding.DialogDefaultBinding

fun FragmentActivity.showDefaultDialog(message: String) {
    val binding = DialogDefaultBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root, Gravity.BOTTOM)

    binding.message.text = message
    postDelayed({
        dialog.dismiss()
    }, 2200)
}