package com.bestDate.fragment.profilePhotoEditor

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.postDelayed
import com.bestDate.data.extension.toStringFormat
import com.bestDate.data.locale.RegistrationDataHolder
import com.bestDate.databinding.FragmentProfilePhotoEditingBinding
import com.bestDate.fragment.BaseFragment
import com.bestDate.view.bottomSheet.imageSheet.ImageListSheet
import com.bestDate.view.bottomSheet.photoEditorSheet.PhotoEditorSheet
import com.bestDate.view.bottomSheet.photoSettingsSheet.PhotoSettingsSheet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import jp.wasabeef.blurry.Blurry

class ProfilePhotoEditingFragment : BaseFragment<FragmentProfilePhotoEditingBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentProfilePhotoEditingBinding = { inflater, parent, attach ->
        FragmentProfilePhotoEditingBinding.inflate(inflater, parent, attach) }

    override val statusBarLight = true

    private lateinit var pagerAdapter: ImagesPageAdapter
    private var imageListSheet: ImageListSheet = ImageListSheet()
    private var photoEditorSheet: PhotoEditorSheet = PhotoEditorSheet()
    private var photoSettingsSheet: PhotoSettingsSheet = PhotoSettingsSheet()
    private var imageList: MutableList<Uri> = ArrayList()

    override fun onInit() {
        super.onInit()

        pagerAdapter = ImagesPageAdapter(imageList, imageClick())

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
                if (imageList.size < 9) {
                    imageListSheet.show(childFragmentManager, imageListSheet.tag)
                } else {
                    showMessage("You can upload only 9 photo")
                }
            }
            backButton.setOnClickListener {
                navController.popBackStack()
            }
            nextButton.setOnClickListener {
                if (imageList.size > 0) {
                    //TODO: go to next page
                }
            }

            imageListSheet.itemClick = {
                imageListSheet.dismiss()
                imageList.add(it)
                nextButton.isVisible = true
                if (imageList.isNotEmpty()) {
                    pagerAdapter.notifyItemChanged((imageList.size - 1) / 3)
                }

                photoSettingsSheet.selectedImage = it
                photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
            }

            photoSettingsSheet.saveClick = {
                photoSettingsSheet.dismiss()
                setMainImage(it)
            }
            photoSettingsSheet.editClick = {
                photoSettingsSheet.dismiss()
                photoEditorSheet.show(childFragmentManager, photoEditorSheet.tag)
            }
            photoSettingsSheet.deleteClick = { uri ->
                photoSettingsSheet.dismiss()
                val index = imageList.indexOfFirst { it == uri }
                imageList.removeAt(index)
                pagerAdapter.notifyDataSetChanged()
            }
            photoSettingsSheet.sendMessage = {
                showMessage(it)
            }
        }
    }

    private fun setMainImage(image: Uri) {
        Glide.with(requireContext())
            .load(image)
            .circleCrop()
            .into(binding.avatar)

        binding.imageBack.visibility = View.INVISIBLE

        Glide.with(requireContext())
            .load(image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?, target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any?, target: Target<Drawable>?,
                    dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    createBlur()
                    return false
                }
            })
            .into(binding.imageBack)
    }

    private fun createBlur() {
        with(binding) {
            postDelayed({
                val bitmap = Blurry.with(requireContext())
                    .radius(20)
                    .sampling(5)
                    .capture(imageBack).get()
                imageBack.setImageDrawable(BitmapDrawable(resources, bitmap))

                overlay.visibility = View.VISIBLE
                imageBack.visibility = View.VISIBLE
            }, 100)

            makeStatusBarTransparent(binding.scroll)

            val color = ContextCompat.getColor(requireContext(), R.color.white)
            headerTitle.setTextColor(color)
            back.setTextColor(color)
            backArrow.setColorFilter(color)
            nextButton.setTextColor(color)
        }


    }

    private fun imageClick(): (Uri) -> Unit {
        return {
            photoSettingsSheet.selectedImage = it
            photoSettingsSheet.show(childFragmentManager, photoSettingsSheet.tag)
        }
    }
}