package com.bestDate.presentation.main.anotherProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentAnotherProfileBinding
import com.bestDate.db.entity.Invitation
import com.bestDate.view.alerts.showCreateInvitationDialog
import com.bestDate.view.bottomSheet.anotherProfileAdditional.AnotherProfileAdditionalBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnotherProfileFragment :
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
    private val args by navArgs<AnotherProfileFragmentArgs>()

    private var user: ShortUserData? = null
    private var invitationList: MutableList<Invitation> = mutableListOf()

    override fun onInit() {
        super.onInit()
        makeStatusBarTransparent(binding.header.getTopBoxView())
        user = args.user
        viewModel.getUserById(user?.id)
        viewModel.refreshInvitations()
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
        binding.navBox.cardClick = {
            requireActivity().showCreateInvitationDialog(invitationList) {
                viewModel.sendInvitation(user?.id, it.id)
            }
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

        viewModel.invitations.observe(viewLifecycleOwner) {
            invitationList = it
        }
        viewModel.sendInvitationLiveData.observe(viewLifecycleOwner) {
            showMessage("Invitation is send successfully")
        }
    }
}