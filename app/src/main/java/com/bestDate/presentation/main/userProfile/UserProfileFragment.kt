package com.bestDate.presentation.main.userProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.model.Image
import com.bestDate.data.model.ProfileImage
import com.bestDate.data.utils.DeeplinkCreator
import com.bestDate.databinding.FragmentUserProfileBinding
import com.bestDate.presentation.base.BasePhotoEditorFragment
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.bottomSheet.ShareMainProfileBottomSheet
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
            goBack()
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
                UserProfileFragmentDirections.actionProfileToMatchesList()
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
            val photos = viewModel.user.value?.photos
            if (photos?.isNotEmpty() == true) {
                navController.navigate(
                    UserProfileFragmentDirections.actionProfileToSlider(photos.toTypedArray())
                )
            } else {
                openImageSelector()
            }
        }
        adapter.addClick = {
            if (adapter.itemCount < 10) {
                openImageSelector()
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
            navController.navigate(UserProfileFragmentDirections.actionGlobalProfileToSettings())
        }
        binding.questionnaireButton.click = {
            navController.navigate(UserProfileFragmentDirections.actionProfileToQuestionnaire())
        }
        binding.additionalAction.setOnSaveClickListener {
            val sheet = ShareMainProfileBottomSheet()

            sheet.shareClick = {
                DeeplinkCreator(
                    viewModel.user.value?.id,
                    viewModel.user.value?.name
                ).get().share(requireContext())
            }

            sheet.show(childFragmentManager)
        }
    }

    private fun openImageSelector() {
        if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable()) showPhotoPicker()
        else imageListSheet.show(childFragmentManager, imageListSheet.tag)
    }

    private fun showPhotoPicker() {
        picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val picker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            val image = Image(uri = it)
            navController.navigate(UserProfileFragmentDirections.actionProfileToPhotoEditor(image))
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.user) {
            it.let { user ->
                user?.getMainPhoto()?.let { image ->
                    setMainImage(image)
                }
                with(binding) {
                    name.text = user?.name
                    birthdate.text = user?.getFormattedBirthday()
                    verifyView.isVerified = user?.questionnaireFull()
                }

                adapter.submitList(getImageList(user?.photos)) {
                    binding.refreshView.isRefreshing = false
                }
            }
        }
        observe(viewModel.hasNewLikes) {
            binding.likeListButton.badgeOn = it
        }
        observe(viewModel.hasNewMatches) {
            binding.matchesListButton.badgeOn = it
        }
        observe(viewModel.hasNewDuels) {
            binding.myDuelsButton.badgeOn = it
        }
        observe(viewModel.hasNewInvitations) {
            binding.invitationListButton.badgeOn = it
        }
        observe(viewModel.coins) {
            binding.balanceButton.coinsCount = it?.toInt().orZero
        }
        observe(viewModel.signOutLiveData) {
            navController.navigate(UserProfileFragmentDirections.actionGlobalAuthFragment())
        }
        observe(BasePhotoEditorFragment.editorAction) {
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
        list.add(ProfileImage(viewType = ProfileImage.ViewType.ADD))
        return list
    }

    private fun setMainImage(image: ProfileImage?) {
        if (image == null) return
        makeStatusBarTransparent(binding.refreshView)

        Glide.with(requireContext())
            .load(image.thumb_url)
            .circleCrop()
            .into(binding.avatar)

        Glide.with(requireContext())
            .load(image.thumb_url)
            .centerCrop()
            .into(binding.imageBackThumb)

        Glide.with(requireContext())
            .load(image.full_url)
            .centerCrop()
            .into(binding.imageBack)
    }

    override fun networkIsUpdated() {
        super.networkIsUpdated()
        viewModel.updateUserData()
    }
}