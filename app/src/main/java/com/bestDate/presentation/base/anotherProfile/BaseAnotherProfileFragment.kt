package com.bestDate.presentation.base.anotherProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.ShortUserData
import com.bestDate.data.utils.DeeplinkCreator
import com.bestDate.databinding.FragmentAnotherProfileBinding
import com.bestDate.db.entity.Invitation
import com.bestDate.db.entity.UserDB
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.alerts.showCreateInvitationDialog
import com.bestDate.view.bottomSheet.anotherProfileAdditional.AnotherProfileAdditionalBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseAnotherProfileFragment :
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

    private var isBlocked: Boolean = false
    protected var user: ShortUserData? = null
    protected var fullUser: UserDB? = null
    private var invitationList: MutableList<Invitation> = mutableListOf()

    abstract fun getBackScreen(): BackScreenType
    abstract fun navigateToChat()
    abstract fun navigateToSlider(position: Int)
    abstract fun navigateToQuestionnaire()

    override fun onInit() {
        super.onInit()
        makeStatusBarTransparent(binding.header.getTopBoxView())
        setBackground(user?.blocked_me)
        binding.header.setUserInfo(user)
        binding.userInfoView.setUserInfo(user)
        binding.userBlockedView.setUserInfo(user)
        binding.navBox.isVisible = user?.blocked_me != true
        binding.navBox.isLiked = user?.getMainPhoto()?.liked == true
        binding.navBox.hasMainPhoto = user?.main_photo != null
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
        binding.navBox.chatClick = {
            if (getBackScreen() == BackScreenType.CHAT) {
                navController.popBackStack()
            } else {
                navigateToChat()
            }
        }
        binding.navBox.likeClick = {
            user?.getMainPhoto()?.id?.let { viewModel.like(it) }
        }
        binding.header.clickAdditionally = {
            val sheet = AnotherProfileAdditionalBottomSheet(isBlocked)
            sheet.show(childFragmentManager, sheet.tag)

            sheet.shareClick = {
                DeeplinkCreator(user?.id, user?.name).get().share(requireContext())
            }
            sheet.blockClick = {
                if (isBlocked) viewModel.unlockUser(user?.id)
                else viewModel.blockUser(user?.id)
            }
            sheet.complainClick = {
                viewModel.complain(user?.id)
            }
        }
        binding.header.clickAvatar = {
            fullUser?.photos?.let {
                if (it.firstOrNull()?.id.orZero > 0) {
                    navigateToSlider(0)
                }
            }
        }
        binding.userInfoView.imageClick = { photo ->
            val position = fullUser?.photos?.indexOfFirst { it.id == photo?.id } ?: 0
            fullUser?.photos?.let {
                navigateToSlider(position)
            }
        }
        binding.userInfoView.openQuestionnaire = {
            navigateToQuestionnaire()
        }
    }

    override fun goBack() {
        viewModel.clearUserData()
        if (getBackScreen() == BackScreenType.SEARCH) {
            setNavigationResult(NavigationResultKey.SAVE_POSITION, true)
        }
        else if (getBackScreen() == BackScreenType.DUELS) {
            setNavigationResult(NavigationResultKey.CHECK_GENDER, true)
        }
        super.goBack()
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.user) {
            it?.let { setFullUserInfo(it) }
        }
        observe(viewModel.blockLiveData) {
            val message = if (it) R.string.user_is_blocked_successful
            else R.string.user_is_unlocked_successful
            showMessage(getString(message))
        }
        observe(viewModel.complainLiveData) {
            showMessage(getString(R.string.complain_is_send_successful))
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

    protected fun setFullUserInfo(fullUser: UserDB) {
        user = fullUser.getShortUser(user)
        setBackground(fullUser.blocked_me)
        binding.header.setUserInfo(fullUser)
        binding.userInfoView.setUserInfo(fullUser)
        binding.userBlockedView.setUserInfo(fullUser)
        binding.navBox.isVisible = fullUser.blocked_me != true
        binding.navBox.isLiked = fullUser.getMainPhoto().liked == true
        isBlocked = fullUser.blocked == true
        this.fullUser = fullUser
        validateChatUser()
    }

    private fun validateChatUser() {
        if (getBackScreen() == BackScreenType.CHAT) {
            setNavigationResult(NavigationResultKey.USER, user)
        }
    }
}