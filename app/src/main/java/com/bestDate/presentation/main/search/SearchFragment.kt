package com.bestDate.presentation.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.model.FilterOptions
import com.bestDate.databinding.FragmentSearchBinding
import com.bestDate.view.bottomSheet.optionsSheet.OptionsSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseVMFragment<FragmentSearchBinding, SearchViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding =
        { inflater, parent, attach -> FragmentSearchBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<SearchViewModel> = SearchViewModel::class.java

    override val navBarColor = R.color.main_dark
    override val statusBarColor = R.color.main_dark
    private val adapter: SearchAdapter = SearchAdapter()
    private lateinit var locationOptionsSheet: OptionsSheet
    private lateinit var statusOptionsSheet: OptionsSheet
    private lateinit var locationMap: HashMap<String, String>
    private lateinit var statusesMap: HashMap<String, String>

    override val statusBarLight = false
    override val navBarLight = false

    override fun onInit() {
        super.onInit()
        locationMap = viewModel.getLocationMap(requireContext())
        statusesMap = viewModel.getStatusesMap(requireContext())
        setUpLocationSheet()
        setUpStatusSheet()
        setUpSwipe()
        setUpToolbar()
        setUpRV()
        setUpFilters()
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        getUsersByFilter()
        viewModel.usersListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.search)
        //binding.toolbar.photo = viewModel.user.value?.photos?.first { it.main == true }?.thumb_url
    }

    private fun setUpSwipe() {
        binding.swipeRefresh.setOnRefreshListener { getUsersByFilter() }
    }

    private fun setUpLocationSheet() {
        locationOptionsSheet = OptionsSheet(
            locationMap, getString(R.string.location)
        )
        locationOptionsSheet.itemClick = {
            binding.locationFilterButton.label = it
            getUsersByFilter()
        }
    }

    private fun setUpStatusSheet() {
        statusOptionsSheet = OptionsSheet(
            statusesMap, getString(R.string.online)
        )
        statusOptionsSheet.itemClick = {
            binding.statusFilterButton.label = it
            getUsersByFilter()
        }
    }

    private fun setUpRV() {
        binding.recyclerViewSearches.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewSearches.adapter = adapter
    }

    private fun setUpFilters() {
        binding.run {
            locationFilterButton.label = getString(R.string.all_world)
            locationFilterButton.isActive = true
            locationFilterButton.onClick = {
                locationOptionsSheet.show(childFragmentManager, locationOptionsSheet.tag)
            }
            statusFilterButton.label = getString(R.string.not_selected)
            statusFilterButton.isActive = false
            statusFilterButton.onClick = {
                statusOptionsSheet.show(childFragmentManager, statusOptionsSheet.tag)
            }
        }
    }

    private fun getUsersByFilter() {
        viewModel.getUsers(
            FilterOptions(
                locationMap[binding.locationFilterButton.label]?.lowercase() ?: "",
                statusesMap[binding.statusFilterButton.label]?.lowercase() ?: ""
            )
        )
    }
}