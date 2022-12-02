package com.bestDate.base

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bestDate.R
import com.bestDate.data.extension.setMarginTop
import com.bestDate.view.alerts.showDefaultDialog
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
        try {
            onInit()
            onViewLifecycle()
            onViewClickListener()
            keyboardAction()
        } catch (e: Exception) {
            showMessage(e.message.orEmpty())
        }
    }

    protected open fun onInit() {
        reDrawBars()
    }

    protected open fun onViewLifecycle() { }

    protected open fun onViewClickListener() { }

    open fun onCustomBackNavigation() {
        goBack()
    }

    protected open fun goBack() {
        navController.popBackStack()
    }

    private fun setNavBarColor() {
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), navBarColor)
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), statusBarColor)
    }

    private fun setLightBarFlags() {
        requireActivity().window.decorView.systemUiVisibility = when {
            navBarLight && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && statusBarLight -> {
                SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR xor SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            navBarLight && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            statusBarLight -> SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            else -> 0
        }
    }

    protected fun makeStatusBarTransparent(offsetView: View) {
        requireActivity().window.apply {
            decorView.systemUiVisibility = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
                else -> {
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                }
            }
            statusBarColor = Color.TRANSPARENT
            makeStatusBarOffset(offsetView)
        }
    }

    private fun makeStatusBarOffset(offsetView: View) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val top = insets?.getInsets(WindowInsetsCompat.Type.systemGestures())?.top
            offsetView.setMarginTop(top ?: 0)
            insets
        }
    }

    protected fun reDrawBars() {
        setNavBarColor()
        setStatusBarColor()
        setLightBarFlags()
    }

    open val navBarColor: Int = R.color.bg_main
    open val navBarLight: Boolean = false

    open val statusBarColor: Int = R.color.white
    open val statusBarLight: Boolean = false

    private fun keyboardAction() {
        KeyboardVisibilityEvent.setEventListener(requireActivity()) { isOpen ->
            if (isOpen) scrollAction()
        else hideAction() }
    }

    open fun scrollAction() {}
    open fun hideAction() {}

    open fun showMessage(message: String) {
        requireActivity().showDefaultDialog(message)
    }

    open fun showMessage(@StringRes message: Int) {
        showMessage(getString(message))
    }

    fun hideKeyboardAction() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 2)
    }

    fun showKeyboardAction() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
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