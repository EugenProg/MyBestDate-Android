package com.bestDate.view.bottomSheet.photoEditorSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.databinding.SheetPhotoEditorBinding
import com.bestDate.view.base.BaseBottomSheet

class PhotoEditorSheet: BaseBottomSheet<SheetPhotoEditorBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetPhotoEditorBinding =
        { inflater, parent, attach -> SheetPhotoEditorBinding.inflate(inflater, parent, attach) }


}