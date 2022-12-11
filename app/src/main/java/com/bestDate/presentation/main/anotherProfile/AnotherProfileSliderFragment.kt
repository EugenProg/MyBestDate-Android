package com.bestDate.presentation.main.anotherProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentAnotherProfileSliderBinding
import com.bestDate.db.entity.Invitation
import com.bestDate.view.alerts.showCreateInvitationDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnotherProfileSliderFragment :
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
    private val args by navArgs<AnotherProfileSliderFragmentArgs>()

    private var invitationList: MutableList<Invitation> = mutableListOf()

    override fun onInit() {
        super.onInit()
        binding.sliderView.setPhotos(args.photos.toMutableList(), false, args.position)
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
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.invitations.observe(viewLifecycleOwner) {
            invitationList = it
        }
        viewModel.sendInvitationLiveData.observe(viewLifecycleOwner) {
            showMessage(R.string.invitation_is_send_successful)
        }
    }
}