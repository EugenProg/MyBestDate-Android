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
import com.bestDate.R
import com.bestDate.data.extension.getImages
import com.bestDate.data.extension.observe
import com.bestDate.data.model.Image
import com.bestDate.databinding.SheetItemListBinding
import com.bestDate.view.base.BaseBottomSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ImageListSheet : BaseBottomSheet<SheetItemListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetItemListBinding =
        { inflater, parent, attach -> SheetItemListBinding.inflate(inflater, parent, attach) }

    private lateinit var adapter: ImageSheetAdapter
    private var imageList: MutableLiveData<MutableList<Uri>?> = MutableLiveData()
    var itemClick: ((Image) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        adapter = ImageSheetAdapter { itemClick?.invoke(Image(uri = it)) }

        with(binding) {
            progress.isVisible = true
            title.text = getString(R.string.image_list)

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
        observe(imageList) {
            it?.let {
                adapter.submitList(it) {
                    binding.progress.isVisible = false
                    binding.emptyView.isVisible = it.isEmpty()
                }
            }
        }
    }

    private fun openGallery() {
        binding.progress.isVisible = true
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val files: MutableList<File> = mutableListOf()
            files.addAll(File("/sdcard/" + Environment.DIRECTORY_DCIM).getImages())
            files.addAll(File("/sdcard/" + Environment.DIRECTORY_PICTURES).getImages())

            files.sortByDescending { it.lastModified() }

            val images: MutableList<Uri> = ArrayList()

            for (file in files) {
                images.add(file.toUri())
            }
            imageList.postValue(images)
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openGallery()
            } else {
                showMessage(getString(R.string.no_permission))
            }
        }
}