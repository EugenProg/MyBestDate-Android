package com.bestDate.fragment.profilePhotoEditor

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.data.extension.imageIsSet
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.toStringFormat
import com.bestDate.data.locale.RegistrationDataHolder
import com.bestDate.data.model.Image
import com.bestDate.databinding.FragmentProfilePhotoEditingBinding
import com.bestDate.view.bottomSheet.imageSheet.ImageListSheet
import com.bestDate.view.bottomSheet.photoSettingsSheet.PhotoSettingsSheet
import com.bumptech.glide.Glide
import jp.wasabeef.blurry.Blurry

class ProfilePhotoEditingFragment : BaseFragment<FragmentProfilePhotoEditingBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentProfilePhotoEditingBinding = { inflater, parent, attach ->
        FragmentProfilePhotoEditingBinding.inflate(inflater, parent, attach) }

    override val statusBarLight = true

    private lateinit var pagerAdapter: ImagesPageAdapter
    private var imageListSheet: ImageListSheet = ImageListSheet()
    private var photoSettingsSheet: PhotoSettingsSheet = PhotoSettingsSheet()
    private var imageList: MutableLiveData<MutableList<Image>> = MutableLiveData(ArrayList())

    override fun onInit() {
        super.onInit()

        pagerAdapter = ImagesPageAdapter(imageClick())

        with(binding) {
            imagesCarousel.adapter = pagerAdapter

            uploadButton.title = getString(R.string.upload_a_photo)

            name.text = RegistrationDataHolder.username
            birthdate.text = RegistrationDataHolder.birthdate?.toStringFormat()
            gender.text = RegistrationDataHolder.gender
            email.text = RegistrationDataHolder.email
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
                editorAction.value = null
            }
            nextButton.setOnClickListener {
                if (imageList.value?.size.orZero > 0) {
                    //TODO: go to next page
                }
            }

            imageListSheet.itemClick = {
                imageListSheet.dismiss()

                navController.navigate(ProfilePhotoEditingFragmentDirections
                    .actionProfilePhotoEditingFragmentToPhotoEditorFragment(it))
            }

            photoSettingsSheet.setMainClick = {
                photoSettingsSheet.dismiss()
                setMainImage(it)
            }
            photoSettingsSheet.editClick = {
                photoSettingsSheet.dismiss()

                navController.navigate(ProfilePhotoEditingFragmentDirections
                    .actionProfilePhotoEditingFragmentToPhotoEditorFragment(it))
            }
            photoSettingsSheet.deleteClick = {
                photoSettingsSheet.dismiss()
                deleteImageFromList(it)
            }
            photoSettingsSheet.sendMessage = {
                showMessage(it)
            }
        }
    }

    private fun addImageToList(image: Image) {
        val list: MutableList<Image> = ArrayList()
        imageList.value?.let { im -> list.addAll(im) }
        list.add(image)
        imageList.value = list
    }

    private fun deleteImageFromList(image: Image) {
        val list: MutableList<Image> = ArrayList()
        imageList.value?.let { im -> list.addAll(im) }
        val index = list.indexOfFirst { it.uri == image.uri }
        list.removeAt(index)
        imageList.value = list

        binding.nextButton.isVisible = !imageList.value.isNullOrEmpty()

        if (image.isMain) {
            if (!imageList.value.isNullOrEmpty()) {
                imageList.value?.get(0)?.let { setMainImage(it) }
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        imageList.observe(viewLifecycleOwner) {
            pagerAdapter.submitList(getPageImages(it))
        }
        editorAction.observe(viewLifecycleOwner) {
            if (it != null) {
                addImageToList(it)
                binding.nextButton.isVisible = true
                photoSettingsSheet.selectedImage = it
                photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
            }
        }
    }

    private fun getPageImages(images: MutableList<Image>): MutableList<MutableList<Image>> {
        val pages: MutableList<MutableList<Image>> = ArrayList()
        val pageList: MutableList<Image> = ArrayList()

        for (index in images.indices) {
            pageList.add(images[index])
            if (pageList.size >= 3) {
                val list: MutableList<Image> = ArrayList()
                list.addAll(pageList)
                pages.add(list)
                pageList.clear()
            }
        }
        if (pageList.size < 3) pages.add(pageList)

        return pages
    }

    private fun setMainImage(image: Image) {
        for (photo in imageList.value ?: ArrayList()) {
            if (photo.uri == image.uri) image.isMain = true
            else photo.isMain = false
        }

        Glide.with(requireContext())
            .load(image.bitmap ?: image.uri)
            .circleCrop()
            .into(binding.avatar)

        binding.imageBack.visibility = View.INVISIBLE

        Glide.with(requireContext())
            .load(image.bitmap ?: image.uri)
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

    private fun imageClick(): (Image) -> Unit {
        return {
            photoSettingsSheet.selectedImage = it
            photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
        }
    }

    companion object {
        var editorAction: MutableLiveData<Image?> = MutableLiveData()
    }
}