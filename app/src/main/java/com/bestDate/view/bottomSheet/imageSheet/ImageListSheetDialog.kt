package com.bestDate.view.bottomSheet.imageSheet

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bestDate.data.extension.getImages
import com.bestDate.databinding.ListSheetDialogBinding
import com.bestDate.view.base.BaseBottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ImageListSheetDialog: BaseBottomSheetDialog<ListSheetDialogBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> ListSheetDialogBinding =
        { inflater, parent, attach -> ListSheetDialogBinding.inflate(inflater, parent, attach) }

    private lateinit var adapter: ImageSheetAdapter
    private var imageList: MutableLiveData<MutableList<Uri>> = MutableLiveData(ArrayList())
    var itemClick: ((Uri) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        adapter = ImageSheetAdapter { itemClick?.invoke(it) }

        with(binding) {
            progress.isVisible = true
            title.text = "Image list"

            itemList.layoutManager = GridLayoutManager(requireContext(), 3)
            itemList.adapter = adapter
        }

        if (!allPermissionsGranted()) {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            openGallery()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        imageList.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.progress.isVisible = it.isEmpty()
            }
        }
    }

    private fun openGallery() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val files = File("/sdcard/" + Environment.DIRECTORY_DCIM).getImages()

            files.sortByDescending { it.lastModified() }

            val images: MutableList<Uri> = ArrayList()

            for (file in files) {
                images.add(file.toUri())
            }
            imageList.postValue(images)
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            openGallery()
        } else {
            //showMessage(getString(R.string.no_permission))
        }
    }
}