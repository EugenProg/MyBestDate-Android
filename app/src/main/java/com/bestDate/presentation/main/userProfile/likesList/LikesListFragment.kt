package com.bestDate.presentation.main.userProfile.likesList

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentLikesListBinding

class LikesListFragment: BaseVMFragment<FragmentLikesListBinding, LikesListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLikesListBinding = {
        inflater, parent, attach -> FragmentLikesListBinding.inflate(inflater, parent, attach)
    }
    override val viewModelClass: Class<LikesListViewModel> = LikesListViewModel::class.java

    override val statusBarColor = R.color.bg_main

}