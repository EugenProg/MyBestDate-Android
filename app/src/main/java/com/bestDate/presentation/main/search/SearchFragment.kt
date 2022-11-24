package com.bestDate.presentation.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
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
    override val statusBarColor = R.color.main_dark
    private val adapter: SearchAdapter = SearchAdapter()

    override val statusBarLight = false
    override val navBarLight = false

    override fun onInit() {
        super.onInit()
        with(binding) {
            toolbar.title = getString(R.string.search)
            binding.recyclerViewSearches.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.recyclerViewSearches.adapter = adapter
            adapter.submitList(
                mutableListOf(
                    ProfileData(0, "Alice", "Almaty, Kazakhstan", "25", "2.1"),
                    ProfileData(1, "Alice2", "Almaty, Kazakhstan", "25", "2.1"),
                    ProfileData(2, "Alice3", "Almaty, Kazakhstan", "25", "2.1")
                )
            )
        }
    }
}