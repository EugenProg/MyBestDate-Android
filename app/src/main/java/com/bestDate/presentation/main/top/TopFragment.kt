package com.bestDate.presentation.main.top

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentTopBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopFragment : BaseVMFragment<FragmentTopBinding, TopViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTopBinding =
        { inflater, parent, attach -> FragmentTopBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<TopViewModel> = TopViewModel::class.java

    override val navBarColor = R.color.main_dark
    override val statusBarColor= R.color.main_dark

    override val statusBarLight = false
    override val navBarLight = false
}