package com.bestDate.presentation.base.anotherProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.data.extension.NavigationResultKey
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.setNavigationResult
import com.bestDate.databinding.FragmentAnotherProfileSliderBinding
import com.bestDate.db.entity.Invitation
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.alerts.showCreateInvitationDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class AnotherProfileSliderFragment :
    BaseVMFragment<FragmentAnotherProfileSliderBinding, AnotherProfileViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAnotherProfileSliderBinding =
        { inflater, parent, attach ->
            FragmentAnotherProfileSliderBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val statusBarColor = R.color.bg_main
    override val viewModelClass: Class<AnotherProfileViewModel> =
        AnotherProfileViewModel::class.java

    abstract fun getPosition(): Int
    abstract fun getUserId(): Int

    private var invitationList: MutableList<Invitation> = mutableListOf()
    private var isLikeClicked: Boolean = false

    override fun onInit() {
        super.onInit()
        binding.sliderView.setPhotos(viewModel.photos.value, false, getPosition())
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.back.onClick = {
            goBack()
        }
        binding.navBox.cardClick = {
            requireActivity().showCreateInvitationDialog(invitationList) {
                viewModel.sendInvitation(viewModel.user.value?.id, it.id)
            }
        }
        binding.sliderView.onSwipe = {
            goBack()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.invitations) {
            invitationList = it
        }
        observe(viewModel.sendInvitationLiveData) {
            showMessage(R.string.invitation_is_send_successful)
        }
        observe(viewModel.likeLiveData) {
            isLikeClicked = true
            viewModel.getUserById(getUserId())
            setNavigationResult(NavigationResultKey.RELOAD, true)
        }
        observe(viewModel.photos) { photos ->
            binding.sliderView.setPhotos(photos, false,
                if (isLikeClicked) binding.sliderView.position else getPosition()
            )
            binding.sliderView.handleIsLiked = { position ->
                binding.navBox.isLiked = photos?.get(position)?.liked ?: false
            }
            binding.navBox.likeClick = {
                photos?.get(binding.sliderView.position)?.id?.let {
                    viewModel.like(it)
                }
            }
        }
    }
}