package com.bestDate.presentation.main.userProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.databinding.FragmentUserProfileSliderBinding

class UserProfileSliderFragment: BaseFragment<FragmentUserProfileSliderBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUserProfileSliderBinding =
        { inflater, parent, attach -> FragmentUserProfileSliderBinding.inflate(inflater, parent, attach) }

    override val statusBarColor = R.color.bg_main
    private val args by navArgs<UserProfileSliderFragmentArgs>()

    override fun onInit() {
        super.onInit()
        binding.sliderView.setPhotos(args.photos.toMutableList(), true, 0)
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.back.onClick = {
            goBack()
        }
    }
}