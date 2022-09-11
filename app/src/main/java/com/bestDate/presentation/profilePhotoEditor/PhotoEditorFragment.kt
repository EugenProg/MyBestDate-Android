package com.bestDate.presentation.profilePhotoEditor

import android.content.res.Resources
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.extension.cropListener
import com.bestDate.data.extension.getBitmap
import com.bestDate.data.extension.scale
import com.bestDate.data.model.Image
import com.bestDate.data.utils.FaceDetectorUtils
import com.bestDate.databinding.FragmentPhotoEditorBinding
import com.bestDate.view.bottomSheet.NotCorrectPhotoSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoEditorFragment : BaseVMFragment<FragmentPhotoEditorBinding, ProfilePhotoViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoEditorBinding =
        { inflater, parent, attach -> FragmentPhotoEditorBinding.inflate(inflater, parent, attach) }

    override val viewModelClass: Class<ProfilePhotoViewModel> = ProfilePhotoViewModel::class.java

    override val statusBarLight = false
    override val statusBarColor = R.color.bg_main
    private val navArgs by navArgs<PhotoEditorFragmentArgs>()

    private val notCorrectSheet = NotCorrectPhotoSheet()

    var selectedImage: Image? = null
    var imageLiveData: MutableLiveData<Bitmap> = MutableLiveData()

    override fun onInit() {
        super.onInit()

        selectedImage = navArgs.selectedImage.getImageCopy()

        with(binding) {
            editorContainer.layoutParams.height = Resources.getSystem().displayMetrics.widthPixels
            editorContainer.requestLayout()

            saveButton.title = getString(R.string.save)
            saveButton.isEnabled = false

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
            binding.saveButton.toggleActionEnabled(true)
            binding.photoEditor.crop()
        }
        binding.backButton.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        binding.photoEditor.cropListener(success = successCrop())

        imageLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                saveButton.isEnabled = true
                progress.visibility = View.INVISIBLE
                photoEditor.setBitmap(it)
            }
        }

        viewModel.loadingMode.observe(viewLifecycleOwner) {
            binding.saveButton.toggleActionEnabled(it)
        }
        viewModel.photoSaveLiveData.observe(viewLifecycleOwner) {
            ProfilePhotoEditingFragment.editorAction.value = it
            navController.popBackStack()
        }
        viewModel.errorLive.observe(viewLifecycleOwner) {
            showMessage(it.exception.message)
        }
    }

    private fun successCrop(): (Bitmap) -> Unit {
        return {
            binding.saveButton.toggleActionEnabled(false)
            val detector = FaceDetectorUtils()
            detector.detectFaces(it)

            detector.completion = { faces ->
                if (faces == 1) {
                    viewModel.savePhoto(it)
                } else {
                    notCorrectSheet.show(childFragmentManager, notCorrectSheet.tag)
                }
            }
        }
    }
}