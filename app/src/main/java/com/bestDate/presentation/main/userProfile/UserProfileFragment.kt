package com.bestDate.presentation.main.userProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.presentation.base.BasePhotoEditorFragment
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.toPx
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.FragmentUserProfileBinding
import com.bestDate.view.bottomSheet.imageSheet.ImageListSheet
import com.bestDate.view.bottomSheet.photoSettingsSheet.PhotoSettingsSheet
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : BaseVMFragment<FragmentUserProfileBinding, UserProfileViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUserProfileBinding =
        { inflater, parent, attach ->
            FragmentUserProfileBinding.inflate(inflater, parent, attach)
        }
    override val viewModelClass: Class<UserProfileViewModel> = UserProfileViewModel::class.java
    override val statusBarLight = true
    override val statusBarColor = R.color.bg_main

    private lateinit var adapter: ImageLineAdapter
    private var imageListSheet: ImageListSheet = ImageListSheet()
    private var mainPhotoUrl: String = ""

    override fun onInit() {
        super.onInit()

        val height = (resources.displayMetrics.widthPixels - 42.toPx()) / 3
        adapter = ImageLineAdapter(height, false)
        binding.imagesCarousel.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.imagesCarousel.adapter = adapter

        binding.refreshView.setOnRefreshListener {
            viewModel.updateUserData()
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.backButton.onClick = {
            navController.popBackStack()
        }
        binding.signOutButton.onClick = {
            binding.signOutButton.toggleActionEnabled(true)
            viewModel.signOut()
        }
        binding.likeListButton.click = {
            navController.navigate(UserProfileFragmentDirections.actionProfileToLikesList())
        }
        binding.matchesListButton.click = {
            navController.navigate(
                UserProfileFragmentDirections.actionProfileToMatchesList(
                    mainPhotoUrl
                )
            )
        }
        binding.invitationListButton.click = {
            navController.navigate(
                UserProfileFragmentDirections.actionProfileToInvitationList()
            )
        }
        binding.myDuelsButton.click = {
            navController.navigate(UserProfileFragmentDirections.actionProfileToMyDuels())
        }
        binding.avatarContainer.setOnSaveClickListener {
            viewModel.user.value?.photos?.toTypedArray()?.let {
                navController.navigate(UserProfileFragmentDirections.actionProfileToSlider(it))
            }
        }
        adapter.addClick = {
            if (adapter.itemCount < 10) {
                imageListSheet.show(childFragmentManager, imageListSheet.tag)
            } else {
                showMessage(getString(R.string.you_can_upload_only_9_photo))
            }
        }

        adapter.imageClick = {
            val photoSettingsSheet = PhotoSettingsSheet()
            photoSettingsSheet.setSelectedImage(it)
            photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
        }

        imageListSheet.itemClick = {
            imageListSheet.dismiss()

            navController.navigate(UserProfileFragmentDirections.actionProfileToPhotoEditor(it))
        }

        binding.personalDataButton.click = {
            navController.navigate(UserProfileFragmentDirections.actionProfileToPersonalData())
        }
        binding.settingsButton.click = {
            navController.navigate(UserProfileFragmentDirections.actionProfileToSettings())
        }
        binding.questionnaireButton.click = {
            navController.navigate(UserProfileFragmentDirections.actionProfileToQuestionnaire())
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.user.observe(viewLifecycleOwner) {
            it.let { user ->
                user?.getMainPhoto()?.let { image ->
                    setMainImage(image)
                }
                with(binding) {
                    name.text = user?.name
                    birthdate.text = user?.getFormattedBirthday()
                    verifyView.isVerified = user?.questionnaireFull()
                    matchesListButton.badgeOn = user?.new_matches.orZero > 0
                    invitationListButton.badgeOn = user?.new_invitations.orZero > 0
                    likeListButton.badgeOn = user?.new_likes.orZero > 0
                    myDuelsButton.badgeOn = user?.new_duels.orZero > 0
                    balanceButton.coinsCount = user?.coins?.toInt().orZero
                }

                adapter.submitList(getImageList(user?.photos)) {
                    binding.refreshView.isRefreshing = false
                }
            }
        }
        viewModel.signOutLiveData.observe(viewLifecycleOwner) {
            navController.navigate(UserProfileFragmentDirections.actionGlobalAuthFragment())
        }
        BasePhotoEditorFragment.editorAction.observe(viewLifecycleOwner) {
            if (it != null) {
                val photoSettingsSheet = PhotoSettingsSheet()
                photoSettingsSheet.setSelectedImage(it)
                photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
                BasePhotoEditorFragment.editorAction.value = null
            }
        }
    }

    private fun getImageList(images: MutableList<ProfileImage>?): MutableList<ProfileImage> {
        val list = mutableListOf<ProfileImage>()
        list.addAll(images ?: mutableListOf())
        list.add(ProfileImage(id = -1))
        return list
    }

    private fun setMainImage(image: ProfileImage?) {
        if (image == null) return
        mainPhotoUrl = image.thumb_url.orEmpty()
        makeStatusBarTransparent(binding.refreshView)

        Glide.with(requireContext())
            .load(image.thumb_url)
            .circleCrop()
            .into(binding.avatar)

        Glide.with(requireContext())
            .load(image.full_url)
            .into(binding.imageBack)
    }
}