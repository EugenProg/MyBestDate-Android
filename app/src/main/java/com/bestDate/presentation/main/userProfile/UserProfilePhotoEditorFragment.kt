package com.bestDate.presentation.main.userProfile

import androidx.navigation.fragment.navArgs
import com.bestDate.presentation.base.BasePhotoEditorFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfilePhotoEditorFragment: BasePhotoEditorFragment() {
    private val args by navArgs<UserProfilePhotoEditorFragmentArgs>()
    override fun setSelectedImage() {
        selectedImage = args.selectedImage.getImageCopy()
    }
}