package com.bestDate.presentation.main.matches

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentMatchesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchesFragment : BaseVMFragment<FragmentMatchesBinding, MatchesViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMatchesBinding =
        { inflater, parent, attach -> FragmentMatchesBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<MatchesViewModel> = MatchesViewModel::class.java

    override val navBarColor = R.color.main_dark
    override val statusBarColor= R.color.main_dark

    override val statusBarLight = false
    override val navBarLight = false


    override fun onInit() {
        super.onInit()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.matches)
    }
}