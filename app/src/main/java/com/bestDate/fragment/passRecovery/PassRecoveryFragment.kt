package com.bestDate.fragment.passRecovery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.databinding.FragmentPassRecoveryBinding

class PassRecoveryFragment : BaseFragment<FragmentPassRecoveryBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPassRecoveryBinding =
        { inflater, parent, attach -> FragmentPassRecoveryBinding.inflate(inflater, parent, attach) }

    override val navBarColor = R.color.bg_main
    override val statusBarLight = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            emailInput.hint = getString(R.string.email_or_phone_number)
            recoveryButton.title = getString(R.string.next)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.backButton.setOnClickListener {
            navController.popBackStack()
        }
        binding.recoveryButton.onSafeClick = {
            validate()
        }
    }

    private fun validate() {
        with(binding) {
            when {
                emailInput.text.isBlank() -> emailInput.setError()
                else -> {
                    //TODO: abracadabra
                    /*navController.navigate(PassRecoveryFragmentDirections
                        .actionPassRecoveryFragmentToPassRecoveryOtpFragment(emailInput.text))*/
                }
            }
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        binding.scroll.fullScroll(View.FOCUS_DOWN)
        binding.emailInput.setFocus()
    }
}