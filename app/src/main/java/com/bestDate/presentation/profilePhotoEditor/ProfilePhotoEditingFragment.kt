package com.bestDate.presentation.profilePhotoEditor

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.base.BasePhotoEditorFragment
import com.bestDate.data.extension.imageIsSet
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.FragmentProfilePhotoEditingBinding
import com.bestDate.view.bottomSheet.imageSheet.ImageListSheet
import com.bestDate.view.bottomSheet.photoSettingsSheet.PhotoSettingsSheet
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry

@AndroidEntryPoint
class ProfilePhotoEditingFragment : BaseVMFragment<FragmentProfilePhotoEditingBinding, ProfilePhotoViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentProfilePhotoEditingBinding = { inflater, parent, attach ->
        FragmentProfilePhotoEditingBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<ProfilePhotoViewModel> = ProfilePhotoViewModel::class.java

    override val statusBarLight = true

    private lateinit var pagerAdapter: ImagesPageAdapter
    private var imageListSheet: ImageListSheet = ImageListSheet()
    private var mainPhotoId: Int = 0
    private var imageList: MutableLiveData<MutableList<ProfileImage>> = MutableLiveData(ArrayList())

    override fun onInit() {
        super.onInit()

        pagerAdapter = ImagesPageAdapter(imageClick())

        with(binding) {
            imagesCarousel.adapter = pagerAdapter

            uploadButton.title = getString(R.string.upload_a_photo)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            uploadButton.onClick = {
                if (imageList.value?.size.orZero < 9) {
                    imageListSheet.show(childFragmentManager, imageListSheet.tag)
                } else {
                    showMessage(getString(R.string.you_can_upload_only_9_photo))
                }
            }
            backButton.setOnClickListener {
                navController.popBackStack()
                com.bestDate.presentation.base.BasePhotoEditorFragment.editorAction.value = null
            }
            nextButton.setOnSaveClickListener {
                if (imageList.value?.size.orZero > 0) {
                    navController.navigate(ProfilePhotoEditingFragmentDirections
                        .actionProfilePhotoEditingFragmentToQuestionnaireFragment())
                }
            }

            imageListSheet.itemClick = {
                imageListSheet.dismiss()

                navController.navigate(ProfilePhotoEditingFragmentDirections
                    .actionProfilePhotoEditingFragmentToPhotoEditorFragment(it))
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        imageList.observe(viewLifecycleOwner) {
            pagerAdapter.submitList(getPageImages(it))
        }
        BasePhotoEditorFragment.editorAction.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.nextButton.isVisible = true
                val photoSettingsSheet = PhotoSettingsSheet()
                photoSettingsSheet.setSelectedImage(it)
                photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
            }
        }
        viewModel.user.observe(viewLifecycleOwner) {
            with(binding) {
                name.text = it?.name
                birthdate.text = it?.getFormattedBirthday()
                gender.text = it?.getLocalizeGender()?.let { gender -> getString(gender) }
                email.text = if (it?.email_verification == true) it.email else it?.phone

                imageList.value = it?.photos
                it?.getMainPhoto()?.let { photo -> setMainImage(photo) }

                binding.nextButton.isVisible = !it?.photos.isNullOrEmpty()
            }
        }
    }

    private fun getPageImages(images: MutableList<ProfileImage>): MutableList<MutableList<ProfileImage>> {
        val pages: MutableList<MutableList<ProfileImage>> = ArrayList()
        val pageList: MutableList<ProfileImage> = ArrayList()

        for (index in images.indices) {
            pageList.add(images[index])
            if (pageList.size >= 3) {
                val list: MutableList<ProfileImage> = ArrayList()
                list.addAll(pageList)
                pages.add(list)
                pageList.clear()
            }
        }
        if (pageList.size in 1..3) pages.add(pageList)

        return pages
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
            back.setTextColor(color)
            backArrow.setColorFilter(color)
            nextButton.setTextColor(color)
        }
    }

    private fun imageClick(): (ProfileImage) -> Unit {
        return {
            val photoSettingsSheet = PhotoSettingsSheet()
            photoSettingsSheet.setSelectedImage(it)
            photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
        }
    }
}