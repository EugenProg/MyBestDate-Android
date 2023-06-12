package com.bestDate.presentation.profilePhotoEditor

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.model.Image
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.FragmentProfilePhotoEditingBinding
import com.bestDate.presentation.base.BasePhotoEditorFragment
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.main.userProfile.ImageLineAdapter
import com.bestDate.view.bottomSheet.imageSheet.ImageListSheet
import com.bestDate.view.bottomSheet.photoSettingsSheet.PhotoSettingsSheet
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry

@AndroidEntryPoint
class ProfilePhotoEditingFragment :
    BaseVMFragment<FragmentProfilePhotoEditingBinding, ProfilePhotoViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentProfilePhotoEditingBinding = { inflater, parent, attach ->
        FragmentProfilePhotoEditingBinding.inflate(inflater, parent, attach)
    }
    override val viewModelClass: Class<ProfilePhotoViewModel> = ProfilePhotoViewModel::class.java

    override val statusBarLight = true

    private lateinit var adapter: ImageLineAdapter
    private var imageListSheet: ImageListSheet = ImageListSheet()
    private var mainPhotoId: Int = 0
    private var imageList: MutableLiveData<MutableList<ProfileImage>> = MutableLiveData(ArrayList())

    override fun onInit() {
        super.onInit()

        val height = (resources.displayMetrics.widthPixels - 8.toPx()) / 3
        adapter = ImageLineAdapter(height, true)

        with(binding) {
            imagesCarousel.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            imagesCarousel.adapter = adapter
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            uploadButton.onClick = {
                if (imageList.value?.size.orZero < 9) {
                    if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable()) showPhotoPicker()
                    else imageListSheet.show(childFragmentManager, imageListSheet.tag)
                } else {
                    showMessage(getString(R.string.you_can_upload_only_9_photo))
                }
            }
            backButton.onClick = {
                navController.popBackStack()
                BasePhotoEditorFragment.editorAction.value = null
            }
            nextButton.setOnSaveClickListener {
                viewModel.increaseImageSkipCount()
                navController.navigate(
                    ProfilePhotoEditingFragmentDirections
                        .actionGlobalQuestionnaireFragment()
                )
            }
            adapter.imageClick = {
                val photoSettingsSheet = PhotoSettingsSheet()
                photoSettingsSheet.setSelectedImage(it)
                photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
            }
            imageListSheet.itemClick = {
                imageListSheet.dismiss()

                navController.navigate(
                    ProfilePhotoEditingFragmentDirections
                        .actionProfilePhotoEditingFragmentToPhotoEditorFragment(it)
                )
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(imageList) {
            adapter.submitList(it)
        }
        observe(BasePhotoEditorFragment.editorAction) {
            if (it != null && it.moderated == false) {
                binding.nextButton.isVisible = true
                val photoSettingsSheet = PhotoSettingsSheet()
                photoSettingsSheet.setSelectedImage(it)
                photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
                BasePhotoEditorFragment.editorAction.value = null
            }
        }
        observe(viewModel.user) {
            with(binding) {
                name.text = it?.name
                birthdate.text = it?.getFormattedBirthday()
                gender.text = it?.getLocalizeGender()?.let { gender -> getString(gender) }
                email.text = if (it?.email_verification == true) it.email else it?.phone

                imageList.value = it?.photos
                it?.getMainPhoto()?.let { photo -> setMainImage(photo) }
            }
        }
    }

    private fun showPhotoPicker() {
        picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val picker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            val image = Image(uri = it)
            navController.navigate(
                ProfilePhotoEditingFragmentDirections
                    .actionProfilePhotoEditingFragmentToPhotoEditorFragment(image)
            )
        }
    }

    private fun setMainImage(image: ProfileImage) {
        if (image.id == mainPhotoId) return
        mainPhotoId = image.id.orZero

        Glide.with(requireContext())
            .load(image.thumb_url)
            .circleCrop()
            .into(binding.avatar)

        binding.imageBack.visibility = View.INVISIBLE

        Glide.with(requireContext())
            .load(image.full_url)
            .imageIsSet(this) {
                createBlur()
            }
            .into(binding.imageBack)
    }

    private fun createBlur() {
        with(binding) {
            val bitmap = Blurry.with(requireContext())
                .radius(20)
                .sampling(5)
                .capture(imageBack).get()
            imageBack.setImageDrawable(BitmapDrawable(resources, bitmap))

            overlay.visibility = View.VISIBLE
            imageBack.visibility = View.VISIBLE

            makeStatusBarTransparent(binding.scroll)

            val color = ContextCompat.getColor(requireContext(), R.color.white)
            headerTitle.setTextColor(color)
            backButton.setColor(color)
            nextButton.setTextColor(color)
        }
    }
}