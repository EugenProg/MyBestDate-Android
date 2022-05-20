package com.bestDate.data.extension

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

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