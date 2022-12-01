package com.bestDate.data.extension

import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bestDate.R

fun DialogFragment?.show(fragmentManager: FragmentManager) {
    if (this?.isAdded == false && !this.isVisible) {
        this.show(
            fragmentManager,
            this::class.java.canonicalName
        )
        fragmentManager.executePendingTransactions()
    } else {
        return
    }
}

fun getDialog(
    view: View,
    position: Int = Gravity.CENTER,
    width: Int = LinearLayout.LayoutParams.MATCH_PARENT,
    height: Int = LinearLayout.LayoutParams.WRAP_CONTENT
): Dialog {
    val dialog = Dialog(view.context, R.style.dialog_theme)
    dialog.setContentView(view)
    dialog.window?.setLayout(width, height)
    dialog.window?.setGravity(position)
    dialog.show()
    return dialog
}