package com.bestDate.presentation.profilePhotoEditor

import androidx.navigation.fragment.navArgs
import com.bestDate.presentation.base.BasePhotoEditorFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationPhotoEditorFragment: BasePhotoEditorFragment() {

    private val navArgs by navArgs<RegistrationPhotoEditorFragmentArgs>()
    override fun setSelectedImage() {
        selectedImage = navArgs.selectedImage.getImageCopy()
    }
}