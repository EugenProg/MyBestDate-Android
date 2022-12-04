package com.bestDate.presentation.base.anotherProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentAnotherProfileBinding
import com.bestDate.view.bottomSheet.anotherProfileAdditional.AnotherProfileAdditionalBottomSheet

abstract class AnotherProfileFragment :
    BaseVMFragment<FragmentAnotherProfileBinding, AnotherProfileViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAnotherProfileBinding =
        { inflater, parent, attach ->
            FragmentAnotherProfileBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<AnotherProfileViewModel> = AnotherProfileViewModel::class.java
    override val statusBarLight = true
    var isBlocked: Boolean = false

    protected var user: ShortUserData? = null

    abstract fun getUserInfo(): ShortUserData?

    override fun onInit() {
        super.onInit()
        makeStatusBarTransparent(binding.header.getTopBoxView())
        user = getUserInfo()
        viewModel.getUserById(user?.id)
        setBackground(user?.blocked_me)
        binding.header.setUserInfo(user)
        binding.userInfoView.setUserInfo(user)
        binding.userBlockedView.setUserInfo(user)
        binding.navBox.isVisible = user?.blocked_me != true
        isBlocked = user?.blocked == true
    }

    private fun setBackground(isBlocked: Boolean?) {
        if (isBlocked == true) binding.root.setBackgroundResource(R.drawable.bg_blocked_profile)
        else binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_main))
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        customBackNavigation = true
        binding.header.clickBack = {
            goBack()
        }
        binding.header.clickAdditionally = {
            val sheet = AnotherProfileAdditionalBottomSheet(isBlocked)
            sheet.show(childFragmentManager, sheet.tag)

            sheet.shareClick = {

            }

            sheet.blockClick = {
                if (isBlocked) viewModel.unlockUser(user?.id)
                else viewModel.blockUser(user?.id)
            }
        }
    }

    override fun goBack() {
        viewModel.clearUserData()
        super.goBack()
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.user.observe(viewLifecycleOwner) {
            setBackground(it?.blocked_me)
            binding.header.setUserInfo(it)
            binding.userInfoView.setUserInfo(it)
            binding.userBlockedView.setUserInfo(it)
            binding.navBox.isVisible = it?.blocked_me != true
            isBlocked = it?.blocked == true
        }

        viewModel.blockLiveData.observe(viewLifecycleOwner) {
            val message = if (it) R.string.user_is_blocked_successful
                            else R.string.user_is_unlocked_successful
            showMessage(getString(message))
        }
    }
}