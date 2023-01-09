package com.bestDate.presentation.main.anotherProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentAnotherProfileBinding
import com.bestDate.db.entity.Invitation
import com.bestDate.db.entity.UserDB
import com.bestDate.presentation.base.BaseVMFragment
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
    override val viewModelClass: Class<AnotherProfileViewModel> =
        AnotherProfileViewModel::class.java
    override val statusBarLight = true
    private val args by navArgs<AnotherProfileFragmentArgs>()

    var isBlocked: Boolean = false
    private var user: ShortUserData? = null
    private var fullUser: UserDB? = null
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
        binding.navBox.isLiked = user?.getMainPhoto()?.liked == true
        isBlocked = user?.blocked == true
    }

    private fun setBackground(isBlocked: Boolean?) {
        if (isBlocked == true) binding.root.setBackgroundResource(R.drawable.bg_blocked_profile)
        else binding.root.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.bg_main
            )
        )
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
        binding.navBox.hasMainPhoto = user?.main_photo != null
        binding.navBox.likeClick = {
            user?.getMainPhoto()?.id?.let { viewModel.like(it) }
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
        binding.header.clickAvatar = {
            fullUser?.photos?.toTypedArray()?.let {
                if (it.firstOrNull()?.id.orZero > 0) {
                    navController.navigate(
                        AnotherProfileFragmentDirections
                            .actionAnotherProfileToSlider(0, fullUser?.id ?: 0)
                    )
                }
            }
        }
        binding.userInfoView.imageClick = { photo ->
            val position = fullUser?.photos?.indexOfFirst { it.id == photo?.id } ?: 0
            fullUser?.photos?.toTypedArray()?.let {
                navController.navigate(
                    AnotherProfileFragmentDirections
                        .actionAnotherProfileToSlider(position, fullUser?.id ?: 0)
                )
            }
        }
        binding.userInfoView.openQuestionnaire = {
            navController.navigate(
                AnotherProfileFragmentDirections
                    .actionAnotherProfileToQuestionnaire(fullUser)
            )
        }
    }

    override fun goBack() {
        viewModel.clearUserData()
        super.goBack()
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.user) {
            setBackground(it?.blocked_me)
            binding.header.setUserInfo(it)
            binding.userInfoView.setUserInfo(it)
            binding.userBlockedView.setUserInfo(it)
            binding.navBox.isVisible = it?.blocked_me != true
            binding.navBox.isLiked = it?.getMainPhoto()?.liked == true
            isBlocked = it?.blocked == true
            fullUser = it
        }

        observe(viewModel.blockLiveData) {
            val message = if (it) R.string.user_is_blocked_successful
            else R.string.user_is_unlocked_successful
            showMessage(getString(message))
        }

        observe(viewModel.invitations) {
            invitationList = it
        }

        observe(viewModel.sendInvitationLiveData) {
            showMessage(R.string.invitation_is_send_successful)
        }

        observe(viewModel.likeLiveData) {
            viewModel.getUserById(user?.id)
        }
        getNavigationResult<Boolean>(NavigationResultKey.RELOAD) { result ->
            if (result) {
                setNavigationResult(NavigationResultKey.RELOAD, false)
                viewModel.getUserById(user?.id)
            }
        }
    }
}