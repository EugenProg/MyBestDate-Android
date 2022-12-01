package com.bestDate.presentation.main.userProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.extension.toPx
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.FragmentUserProfileBinding
import com.bestDate.base.BasePhotoEditorFragment
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
        adapter = ImageLineAdapter(height)
        binding.imagesCarousel.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.imagesCarousel.adapter = adapter

        binding.refreshView.setOnRefreshListener {
            viewModel.updateUserData()
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.signOutButton.root.setOnSaveClickListener {
            viewModel.signOut()
        }
        binding.likeListButton.click = {
            navController.navigate(UserProfileFragmentDirections.actionProfileToLikesList())
        }
        binding.myDuelsButton.click = {
            navController.navigate(UserProfileFragmentDirections.actionProfileToMyDuels())
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
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.user.observe(viewLifecycleOwner) {
            it.let { user ->
                user?.photos?.firstOrNull { it.main == true }.let { image ->
                    setMainImage(image)
                }
                binding.name.text = user?.name
                binding.birthdate.text = user?.getFormattedBirthday()

                adapter.submitList(getImageList(user?.photos)) {
                    binding.refreshView.isRefreshing = false
                }
            }
        }
        BasePhotoEditorFragment.editorAction.observe(viewLifecycleOwner) {
            if (it != null) {
                val photoSettingsSheet = PhotoSettingsSheet()
                photoSettingsSheet.setSelectedImage(it)
                photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
            }
        }
    }

    private fun getImageList(images: MutableList<ProfileImage>?): MutableList<ProfileImage> {
        images?.add(ProfileImage(id = -1))
        return images ?: mutableListOf()
    }

    private fun setMainImage(image: ProfileImage?) {
        if (image == null) return
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