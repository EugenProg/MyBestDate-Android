package com.bestDate.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bestDate.R
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

abstract class BaseFragment<VB: ViewBinding>: Fragment() {
    abstract val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewLifecycleOwnerLiveData.observe(this) {
            if (it == null) return@observe
            requireActivity().onBackPressedDispatcher.addCallback(it, backPressedCallback)
        }
    }

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
        navController = findNavController()
        onInit()
        onViewLifecycle()
        onViewClickListener()
    }

    protected open fun onInit() {
        setNavBarColor()
        setStatusBarColor()
    }

    protected open fun onViewLifecycle() { }

    protected open fun onViewClickListener() { }

    open fun onCustomBackNavigation() {}

    private fun setNavBarColor() {
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), navBarColor)
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), statusBarColor)
    }

    open val navBarColor: Int = R.color.bg_main

    open val statusBarColor: Int = R.color.bg_main

    private fun keyboardAction() {
        KeyboardVisibilityEvent.setEventListener(requireActivity()) { isOpen ->
            if (isOpen) scrollAction() }
    }

    open fun scrollAction() {}

    open fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        //requireActivity().showStandardMessage(message)
    }

    fun hideKeyboardAction() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun showKeyboardAction() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun setFocus(view: EditText) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
    }

    protected open var customBackNavigation: Boolean = false
        set(value) {
            field = value
            backPressedCallback.isEnabled = value
        }

    private val backPressedCallback: OnBackPressedCallback by lazy {
        object : OnBackPressedCallback(customBackNavigation) {
            override fun handleOnBackPressed() {
                onCustomBackNavigation()
            }
        }
    }
}