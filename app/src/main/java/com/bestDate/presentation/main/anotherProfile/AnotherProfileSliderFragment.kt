package com.bestDate.presentation.main.anotherProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.databinding.FragmentAnotherProfileSliderBinding

class AnotherProfileSliderFragment: BaseFragment<FragmentAnotherProfileSliderBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAnotherProfileSliderBinding =
        { inflater, parent, attach -> FragmentAnotherProfileSliderBinding.inflate(inflater, parent, attach) }

    override val statusBarColor = R.color.bg_main
    private val args by navArgs<AnotherProfileSliderFragmentArgs>()

    override fun onInit() {
        super.onInit()
        binding.sliderView.setPhotos(args.photos.toMutableList(), false, args.position)
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.back.onClick = {
            goBack()
        }
    }
}