package com.bestDate.view.bottomSheet.imageSheet

import android.Manifest
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.GridLayoutManager
import com.bestDate.R
import com.bestDate.data.model.Image
import com.bestDate.data.utils.ImageFetcherUtil
import com.bestDate.databinding.SheetItemListBinding
import com.bestDate.view.base.BaseBottomSheet

class ImageListSheet : BaseBottomSheet<SheetItemListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetItemListBinding =
        { inflater, parent, attach -> SheetItemListBinding.inflate(inflater, parent, attach) }

    private lateinit var adapter: ImageSheetAdapter
    var itemClick: ((Image) -> Unit)? = null

    private lateinit var imageFetcher: ImageFetcherUtil

    override fun onInit() {
        super.onInit()
        imageFetcher = ImageFetcherUtil(requireContext())
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
            initLoading()
        }
    }

    private fun initLoading() {
        val loaderManager = LoaderManager.getInstance(this)
        loaderManager.initLoader(6535, null, imageFetcher)
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        imageFetcher.imagesLoaded = {
            adapter.submitList(it) {
                binding.progress.isVisible = false
                binding.emptyView.isVisible = it.isEmpty()
            }
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
                initLoading()
            } else {
                showMessage(getString(R.string.no_permission))
            }
        }
}