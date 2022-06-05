package com.bestDate.fragment.passRecovery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.FragmentPassRecoverySetNewBinding

class PassRecoverySetNewFragment : BaseFragment<FragmentPassRecoverySetNewBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPassRecoverySetNewBinding =
        { inflater, parent, attach -> FragmentPassRecoverySetNewBinding.inflate(inflater, parent, attach)}

    override val statusBarLight = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            passInput.isPasswordField = true
            passInput.hint = getString(R.string.enter_a_new_password)

            saveButton.title = getString(R.string.install_and_login)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.setOnSaveClickListener { navController.popBackStack() }

            saveButton.onClick = {
                navController.navigate(PassRecoverySetNewFragmentDirections
                    .actionPassRecoverySetNewFragmentToGeoEnableFragment())
            }
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        with(binding) {
            scroll.fullScroll(View.FOCUS_DOWN)
            passInput.setFocus()
        }
    }
}