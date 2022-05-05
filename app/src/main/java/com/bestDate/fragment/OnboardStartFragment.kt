package com.bestDate.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.databinding.FragmentOnboardStartBinding

class OnboardStartFragment : BaseFragment<FragmentOnboardStartBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboardStartBinding =
        { inflater, parent, attach -> FragmentOnboardStartBinding.inflate(inflater, parent, attach) }

    override val navBarColor = R.color.bg_pink
}