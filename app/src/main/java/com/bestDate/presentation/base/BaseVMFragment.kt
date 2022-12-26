package com.bestDate.presentation.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.bestDate.R
import com.bestDate.data.model.HandleError
import com.bestDate.data.model.InternalException

abstract class BaseVMFragment<VB: ViewBinding, VM: BaseViewModel> : BaseFragment<VB>() {
    protected lateinit var viewModel: VM
    protected abstract val viewModelClass: Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(viewModelClass)
    }

    protected open fun exceptionAction(handler: HandleError) {
        if (handler.exception is InternalException.OperationException) {
            showMessage(handler.exception.message)
        } else showMessage(getString(R.string.oops_its_error))
    }
}