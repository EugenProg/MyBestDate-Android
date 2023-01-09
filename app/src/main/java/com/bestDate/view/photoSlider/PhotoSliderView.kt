package com.bestDate.view.photoSlider

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.bestDate.R
import com.bestDate.data.extension.onPageChanged
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setHeight
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.ViewPhotoSliderBinding
import com.google.android.material.tabs.TabLayoutMediator

class PhotoSliderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val adapter: PhotoSliderAdapter = PhotoSliderAdapter()
    private val binding: ViewPhotoSliderBinding =
        ViewPhotoSliderBinding.inflate(LayoutInflater.from(context), this)

    var photoList: MutableList<ProfileImage>? = mutableListOf()
    private var topPositionVisibility: Boolean = false
    var handleIsLiked: ((Int) -> Unit)? = null
    var onSwipe: (() -> Unit)? = null
    var position: Int = 0

    init {
        with(binding) {
            pager.setHeight(resources.displayMetrics.widthPixels)
            pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            pager.offscreenPageLimit = 1
            pager.adapter = adapter
            TabLayoutMediator(tabLayout, pager) { tab, _ ->
                tab.view.isClickable = true
            }.attach()
            pager.onPageChanged {
                setPhotoSettings(it)
                handleIsLiked?.invoke(it)
                position = it
            }

            adapter.onSwipe = {
                onSwipe?.invoke()
            }
        }
    }

    private fun setPhotoSettings(position: Int) {
        val photo = photoList?.get(position)
        binding.likeCount.text = photo?.likes.toString()
        if (topPositionVisibility && photo?.top_place != null) {
            binding.topPositionBox.isVisible = true
            binding.topPosition.text =
                context.getString(R.string.top_position, photo.top_place.orZero)
        } else {
            binding.topPositionBox.isVisible = false
        }
    }

    fun setPhotos(
        photoList: MutableList<ProfileImage>?,
        showPosition: Boolean,
        selectPosition: Int
    ) {
        this.photoList = photoList
        position = selectPosition
        topPositionVisibility = showPosition
        adapter.submitList(photoList) {
            postDelayed({
                binding.pager.currentItem = selectPosition
                setPhotoSettings(selectPosition)
            }, 100)
        }
    }
}