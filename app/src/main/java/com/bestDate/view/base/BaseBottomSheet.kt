package com.bestDate.view.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bestDate.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Exception

abstract class BaseBottomSheet<VB: ViewBinding>: BottomSheetDialogFragment() {
    abstract val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    var sendMessage: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = onBinding(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            onInit()
            onViewLifecycle()
            onViewClickListener()
        } catch (e: Exception) {

        }
    }

    protected open fun onInit() { }

    protected open fun onViewLifecycle() { }

    protected open fun onViewClickListener() { }

    @SuppressLint("RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.behavior.disableShapeAnimations()
        bottomSheetDialog.setCanceledOnTouchOutside(true)

        return bottomSheetDialog
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    protected fun showMessage(message: String) {
        sendMessage?.invoke(message)
    }
}