package com.bestDate.presentation.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseVMFragment<FragmentSearchBinding, SearchViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding =
        { inflater, parent, attach -> FragmentSearchBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<SearchViewModel> = SearchViewModel::class.java

    override val navBarColor = R.color.main_dark
    override val statusBarColor= R.color.main_dark

    override val statusBarLight = false
    override val navBarLight = false

    override fun onInit() {
        super.onInit()
        viewModel.setNotFirstEnter()
        //todo move to geo loc
    }
}