package com.bestDate.presentation.main.duels.top

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.databinding.FragmentTopBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopFragment : BaseVMFragment<FragmentTopBinding, TopViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTopBinding =
        { inflater, parent, attach -> FragmentTopBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<TopViewModel> = TopViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarColor = R.color.bg_main

    override val statusBarLight = false
    override val navBarLight = false
}