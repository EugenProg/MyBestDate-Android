package com.bestDate.presentation.base

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bestDate.R
import com.bestDate.data.extension.cropListener
import com.bestDate.data.extension.getBitmap
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.scale
import com.bestDate.data.model.Image
import com.bestDate.data.model.ProfileImage
import com.bestDate.data.utils.FaceDetectorUtils
import com.bestDate.databinding.FragmentPhotoEditorBinding
import com.bestDate.presentation.profilePhotoEditor.ProfilePhotoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BasePhotoEditorFragment :
    BaseVMFragment<FragmentPhotoEditorBinding, ProfilePhotoViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoEditorBinding =
        { inflater, parent, attach -> FragmentPhotoEditorBinding.inflate(inflater, parent, attach) }

    override val viewModelClass: Class<ProfilePhotoViewModel> = ProfilePhotoViewModel::class.java

    override val statusBarLight = false
    override val statusBarColor = R.color.bg_main

    abstract fun setSelectedImage()

    var selectedImage: Image? = null
    var imageLiveData: MutableLiveData<Bitmap> = MutableLiveData()

    override fun onInit() {
        super.onInit()
        setSelectedImage()

        with(binding) {
            saveButton.isEnabled = false
            saveButton.loaderVisibility = false

            lifecycleScope.launch(Dispatchers.IO) {
                if (selectedImage?.bitmap == null) {
                    val bitmap = selectedImage?.uri.getBitmap(requireContext())
                    bitmap?.scale()?.let { imageLiveData.postValue(it) }
                } else selectedImage?.bitmap?.let { imageLiveData.postValue(it) }
            }
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.saveButton.onSafeClick = {
            if (!binding.photoEditor.isOffFrame()) {
                binding.saveButton.toggleActionEnabled(true)
                binding.photoEditor.crop()
            } else showMessage(getString(R.string.the_image_is_out_of_frames))
        }
        binding.backButton.onClick = {
            editorAction.value = null
            navController.popBackStack()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        binding.photoEditor.cropListener(success = successCrop(), failure = failureCrop())

        observe(imageLiveData) {
            with(binding) {
                saveButton.isEnabled = true
                saveButton.loaderVisibility = true
                progress.visibility = View.INVISIBLE
                photoEditor.setBitmap(it)
            }
        }

        observe(viewModel.loadingMode) {
            binding.saveButton.toggleActionEnabled(it)
        }
        observe(viewModel.photoSaveLiveData) {
            editorAction.value = it
            navController.popBackStack()
        }
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
        }
    }

    private fun successCrop(): (Bitmap) -> Unit {
        return {
            binding.saveButton.toggleActionEnabled(false)
            val detector = FaceDetectorUtils()
            detector.detectFaces(it)

            detector.completion = { faces ->
                viewModel.savePhoto(faces != 1, it)
            }
        }
    }

    private fun failureCrop(): ((Exception) -> Unit) {
        return {
            binding.saveButton.toggleActionEnabled(false)
        }
    }

    companion object {
        var editorAction: MutableLiveData<ProfileImage?> = MutableLiveData()
    }
}