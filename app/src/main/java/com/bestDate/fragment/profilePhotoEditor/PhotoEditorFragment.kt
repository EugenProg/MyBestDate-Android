package com.bestDate.fragment.profilePhotoEditor

import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.data.model.Image
import com.bestDate.databinding.FragmentPhotoEditorBinding
import com.takusemba.cropme.OnCropListener

class PhotoEditorFragment : BaseFragment<FragmentPhotoEditorBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoEditorBinding =
        { inflater, parent, attach -> FragmentPhotoEditorBinding.inflate(inflater, parent, attach) }

    override val statusBarLight = true
    private val navArgs by navArgs<PhotoEditorFragmentArgs>()

    var selectedImage: Image? = null

    override fun onInit() {
        super.onInit()
        selectedImage = navArgs.selectedImage.getImageCopy()

        with(binding) {
            editorContainer.layoutParams.height = Resources.getSystem().displayMetrics.widthPixels
            editorContainer.requestLayout()

            saveButton.title = getString(R.string.save)

            if (selectedImage?.bitmap == null) photoEditor.setUri(selectedImage?.uri ?: Uri.EMPTY)
            else selectedImage?.bitmap?.let { photoEditor.setBitmap(it) }
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.saveButton.onSafeClick = {
            binding.photoEditor.crop()
        }
        binding.backButton.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        binding.photoEditor.addOnCropListener(object : OnCropListener {
            override fun onFailure(e: Exception) {
                showMessage("Error by image saving")
            }

            override fun onSuccess(bitmap: Bitmap) {
                selectedImage?.bitmap = bitmap

                ProfilePhotoEditingFragment.editorAction.value = selectedImage
                navController.popBackStack()
            }
        })
    }
}