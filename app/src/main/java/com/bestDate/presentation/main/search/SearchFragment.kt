package com.bestDate.presentation.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.model.AdditionalFilters
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.FilterOptions
import com.bestDate.data.model.LocationParams
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.utils.CityListItem
import com.bestDate.databinding.FragmentSearchBinding
import com.bestDate.db.entity.UserDB
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.main.search.distance.DistanceFragment
import com.bestDate.presentation.mainActivity.MainActivity
import com.bestDate.view.bottomSheet.genderFilterSheet.GenderFilterBottomSheet
import com.bestDate.view.bottomSheet.optionsSheet.OptionsSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseVMFragment<FragmentSearchBinding, SearchViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding =
        { inflater, parent, attach -> FragmentSearchBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<SearchViewModel> = SearchViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private lateinit var locationMap: LinkedHashMap<FilterType, String>
    private lateinit var statusesMap: LinkedHashMap<FilterType, String>
    private var selectedLocationFilter: FilterType = FilterType.ALL
    private var selectedStatusFilter: FilterType = FilterType.NOT_SELECTED
    private var selectedGenderFilter: GenderFilter = GenderFilter.ALL
    private var additionalFilters: AdditionalFilters? = null
    private var distance: Int? = null
    private var selectedLocation: CityListItem? = null

    override fun onInit() {
        super.onInit()
        var savePosition = false
        getNavigationResult<Boolean>(NavigationResultKey.SAVE_POSITION) {
            savePosition = it
        }
        setUpToolbar()
        setUpAdditionalFiltersView()
        setUpSearchView()

        if (!savePosition) getAllUsers()
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.userList) {
            binding.searchView.setUsers(it)
            binding.searchView.setMeta(viewModel.meta)
        }

        observe(viewModel.loadingLiveData) {
            binding.searchView.setLoading(it)
        }
        observe(viewModel.user) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
            locationMap = viewModel.getLocationMap(requireContext())
            statusesMap = viewModel.getStatusesMap(requireContext())
            setUpFilters()
            if (!viewModel.isDataValidated) {
                it?.let { validateUserData(it) }
            }
        }
        observe(viewModel.locationLiveData) {
            additionalFilters = AdditionalFilters(LocationParams(distance, it?.lat, it?.lon))
            viewModel.clearData()
            getUsersByFilterInitial()
        }
        observe(viewModel.errorLiveData) {
            binding.searchView.closeLoading()
        }
    }

    private fun validateUserData(user: UserDB) {
        if (user.name.isNullOrBlank() || user.gender.isNullOrBlank() || user.birthday.isNullOrBlank()) {
            viewModel.isDataValidated = true
            navController.navigate(
                SearchFragmentDirections.actionSearchToFillData(
                    name = user.name,
                    gender = user.getGenderType(),
                    birthDate = user.birthday
                )
            )
        }
    }

    override fun networkIsUpdated() {
        super.networkIsUpdated()
        getAllUsers()
    }

    private fun setUpSearchView() {
        with(binding.searchView) {
            openUser = {
                navController.navigate(
                    SearchFragmentDirections
                        .actionGlobalSearchToAnotherProfile(it, BackScreenType.SEARCH)
                )
            }
            getUsersByFilter = {
                viewModel.getUsersPaged(
                    FilterOptions(
                        selectedLocationFilter.serverName,
                        selectedStatusFilter.serverName,
                        selectedGenderFilter.serverName,
                        additionalFilters
                    )
                )
            }
            getUsersByFilterInitial = {
                getUsersByFilterInitial()
            }
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

    private fun getAllUsers() {
        viewModel.getUsers(
            FilterOptions(
                viewModel.getFilter(Preferences.FILTER_LOCATION).serverName,
                viewModel.getFilter(Preferences.FILTER_STATUS).serverName,
                viewModel.getGenderFilter().serverName
            )
        )
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.search)
        binding.toolbar.onProfileClick = {
            navController.navigate(SearchFragmentDirections.actionGlobalSearchToProfile())
        }
    }

    private fun setUpGenderSheet() {
        binding.gender.setOnSaveClickListener {
            val genderSheet = GenderFilterBottomSheet(selectedGenderFilter)

            genderSheet.itemClick = {
                if (it != selectedGenderFilter) {
                    selectedGenderFilter = it
                    binding.gender.text = getString(it.filterName)
                    viewModel.saveFilter(Preferences.FILTER_GENDER, it)
                    viewModel.clearData()
                    getUsersByFilterInitial()
                }
            }

            genderSheet.show(childFragmentManager)
        }
    }

    private fun setUpLocationSheet() {
        val locationOptionsSheet = OptionsSheet(
            locationMap, getString(R.string.location), selectedLocationFilter
        )
        locationOptionsSheet.itemClick = {
            if (it.first != selectedLocationFilter || additionalFilters != null) {
                selectedLocationFilter = it.first
                binding.locationFilterButton.label = it.second
                viewModel.saveFilter(Preferences.FILTER_LOCATION, it.first)
                viewModel.clearData()
                additionalFilters = null
                getUsersByFilterInitial()
            }
        }
        locationOptionsSheet.show(childFragmentManager)
    }

    private fun setUpStatusSheet() {
        val statusOptionsSheet =
            OptionsSheet(statusesMap, getString(R.string.online), selectedStatusFilter)
        statusOptionsSheet.itemClick = {
            if (it.first != selectedStatusFilter || additionalFilters != null) {
                selectedStatusFilter = it.first
                binding.statusFilterButton.label = it.second
                viewModel.saveFilter(Preferences.FILTER_STATUS, it.first)
                viewModel.clearData()
                additionalFilters = null
                getUsersByFilterInitial()
            }
        }
        statusOptionsSheet.show(childFragmentManager)
    }

    private fun setUpFilters() {
        with(binding) {
            selectedLocationFilter = viewModel.getFilter(Preferences.FILTER_LOCATION)
            locationFilterButton.label =
                locationMap[selectedLocationFilter] ?: getString(R.string.all_world)
            locationFilterButton.isActive = true
            locationFilterButton.onClick = {
                setUpLocationSheet()
            }
            selectedStatusFilter = viewModel.getFilter(Preferences.FILTER_STATUS)
            statusFilterButton.label =
                statusesMap[selectedStatusFilter] ?: getString(R.string.not_selected)
            statusFilterButton.isActive = false
            statusFilterButton.onClick = {
                setUpStatusSheet()
            }
            viewModel.getGenderFilter().apply {
                selectedGenderFilter = this
                gender.text = getString(this.filterName)
            }
            setUpGenderSheet()
        }
    }

    private fun getUsersByFilterInitial() {
        viewModel.getUsers(
            FilterOptions(
                selectedLocationFilter.serverName,
                selectedStatusFilter.serverName,
                selectedGenderFilter.serverName,
                additionalFilters
            )
        )
    }
}