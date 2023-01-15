package com.bestDate.presentation.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.bestDate.MainActivity
import com.bestDate.R
import com.bestDate.data.extension.close
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.open
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.AdditionalFilters
import com.bestDate.data.model.FilterOptions
import com.bestDate.data.model.LocationParams
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.utils.CityListItem
import com.bestDate.databinding.FragmentSearchBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.main.search.distance.DistanceFragment
import com.bestDate.view.bottomSheet.optionsSheet.OptionsSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseVMFragment<FragmentSearchBinding, SearchViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding =
        { inflater, parent, attach -> FragmentSearchBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<SearchViewModel> = SearchViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarColor = R.color.bg_main
    private val adapter: SearchAdapter = SearchAdapter()
    private lateinit var locationOptionsSheet: OptionsSheet
    private lateinit var statusOptionsSheet: OptionsSheet
    private lateinit var locationMap: MutableList<Pair<String, String>>
    private lateinit var statusesMap: MutableList<Pair<String, String>>
    override val statusBarLight = false
    override val navBarLight = false
    private var additionalFilters: AdditionalFilters? = null
    private var distance: Int? = null
    private var selectedLocation: CityListItem? = null

    override fun onInit() {
        super.onInit()
        setUpSwipe()
        setUpToolbar()
        setUpUsersList()
        setUpAdditionalFiltersView()
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        clearData()
        getAllUsers()
        observe(viewModel.usersListLiveData) {
            adapter.perPage = viewModel.perPage
            adapter.total = viewModel.total
            adapter.submitList(it) {
                binding.swipeRefresh.isRefreshing = false
                binding.noDataViewWithLoading.noData = it.isEmpty()
            }
        }

        observe(viewModel.loadingLiveData) {
            if (!binding.swipeRefresh.isRefreshing &&
                viewModel.usersListLiveData.value.isNullOrEmpty()
            ) binding.noDataViewWithLoading.toggleLoading(it)
        }
        observe(viewModel.user) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
            locationMap = viewModel.getLocationMap(requireContext())
            statusesMap = viewModel.getStatusesMap(requireContext())
            setUpLocationSheet()
            setUpStatusSheet()
            setUpFilters()
        }
        observe(viewModel.locationLiveData) {
            additionalFilters = AdditionalFilters(LocationParams(distance, it?.lat, it?.lon))
            clearData()
            getUsersByFilterInitial()
        }
    }

    private fun setUpAdditionalFiltersView() {
        binding.filtersView.setOnSaveClickListener {
            val distanceFragment = DistanceFragment(
                viewModel.user.value?.getUserLocation(),
                distance,
                selectedLocation
            )

            distanceFragment.saveClick = { location, distance ->
                viewModel.getLocationByAddress(location)
                this.selectedLocation = location
                this.distance = distance
                closePage(distanceFragment)
            }
            distanceFragment.backClick = {
                closePage(distanceFragment)
            }

            open(distanceFragment, binding.fragmentContainer)
            (activity as MainActivity).bottomNavView?.isVisible = false
        }
    }

    private fun closePage(distanceFragment: DistanceFragment) {
        close(distanceFragment, binding.fragmentContainer) {
            reDrawBars()
            (activity as MainActivity).bottomNavView?.isVisible = true
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.search)
        binding.toolbar.onProfileClick = {
            navController.navigate(R.id.action_global_profile_nav_graph_from_search)
        }
    }

    private fun setUpSwipe() {
        binding.swipeRefresh.setOnRefreshListener {
            clearData()
            getUsersByFilterInitial()
        }
    }

    private fun setUpLocationSheet() {
        locationOptionsSheet = OptionsSheet(
            locationMap, getString(R.string.location)
        )
        locationOptionsSheet.itemClick = {
            binding.locationFilterButton.label = it?.first ?: getString(R.string.all_world)
            saveFilters(Preferences.FILTER_LOCATION, it?.second ?: "all")
            clearData()
            additionalFilters = null
            getUsersByFilterInitial()
        }
    }

    private fun setUpStatusSheet() {
        statusOptionsSheet = OptionsSheet(
            statusesMap, getString(R.string.online)
        )
        statusOptionsSheet.itemClick = {
            binding.statusFilterButton.label = it?.first ?: getString(R.string.not_selected)
            saveFilters(Preferences.FILTER_STATUS, it?.second ?: "all")
            clearData()
            additionalFilters = null
            getUsersByFilterInitial()
        }
    }

    private fun setUpUsersList() {
        binding.recyclerViewSearches.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter.itemClick = {
            navController.navigate(SearchFragmentDirections.actionGlobalAnotherProfileNavGraph2(it))
        }
        adapter.loadMoreItems = {
            getUsersByFilter()
        }
        binding.recyclerViewSearches.adapter = adapter
    }

    private fun setUpFilters() {
        binding.run {
            locationFilterButton.label =
                locationMap.find { it.second == viewModel.getFilter(Preferences.FILTER_LOCATION) }?.first
                    ?: getString(R.string.all_world)
            locationFilterButton.isActive = true
            locationFilterButton.onClick = {
                locationOptionsSheet.show(childFragmentManager, locationOptionsSheet.tag)
            }
            statusFilterButton.label =
                statusesMap.find { it.second == viewModel.getFilter(Preferences.FILTER_STATUS) }?.first
                    ?: getString(R.string.not_selected)
            statusFilterButton.isActive = false
            statusFilterButton.onClick = {
                statusOptionsSheet.show(childFragmentManager, statusOptionsSheet.tag)
            }
        }
    }

    private fun getUsersByFilter() {
        viewModel.getUsersPaged(
            FilterOptions(
                locationMap.firstOrNull { it.first == binding.locationFilterButton.label }?.second?.lowercase()
                    ?: "",
                statusesMap.firstOrNull { it.first == binding.statusFilterButton.label }?.second?.lowercase()
                    ?: "",
                additionalFilters
            )
        )
    }

    private fun getUsersByFilterInitial() {
        viewModel.getUsers(
            FilterOptions(
                locationMap.firstOrNull { it.first == binding.locationFilterButton.label }?.second?.lowercase()
                    ?: "",
                statusesMap.firstOrNull { it.first == binding.statusFilterButton.label }?.second?.lowercase()
                    ?: "",
                additionalFilters
            )
        )
    }

    private fun getAllUsers() {
        viewModel.getUsers(
            FilterOptions(
                viewModel.getFilter(Preferences.FILTER_LOCATION),
                viewModel.getFilter(Preferences.FILTER_STATUS)
            )
        )
    }

    private fun saveFilters(type: Preferences, value: String) {
        viewModel.saveFilter(type, value)
    }

    private fun clearData() {
        viewModel.clearData()
    }
}