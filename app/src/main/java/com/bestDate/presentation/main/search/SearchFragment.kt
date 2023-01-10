package com.bestDate.presentation.main.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import com.bestDate.MainActivity
import com.bestDate.R
import com.bestDate.data.extension.postDelayed
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.AdditionalFilters
import com.bestDate.data.extension.observe
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.data.model.FilterOptions
import com.bestDate.data.model.LocationParams
import com.bestDate.data.preferences.Preferences
import com.bestDate.databinding.FragmentSearchBinding
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
    private lateinit var distanceFragment: DistanceFragment
    override val statusBarLight = false
    override val navBarLight = false
    private var additionalFilters: AdditionalFilters? = null

    override fun onInit() {
        super.onInit()
        setUpSwipe()
        setUpToolbar()
        setUpUsersList()
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
            if (::distanceFragment.isInitialized) closePage(distanceFragment)
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
            setUpAdditionalFiltersView(it?.location?.city)
        }
        viewModel.locationLiveData.observe(viewLifecycleOwner) {
            additionalFilters = AdditionalFilters(LocationParams(5, it?.lat, it?.lon))
            clearData()
            getUsersByFilterInitial()
        }
    }

    private fun setUpAdditionalFiltersView(location: String?) {
        binding.filtersView.setOnSaveClickListener {
            distanceFragment = DistanceFragment(location)
            distanceFragment.saveClick = { viewModel.getLocationByAddress(it) }
            distanceFragment.backClick = { closePage(distanceFragment) }
            childFragmentManager.commit {
                setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out)
                replace(R.id.fragmentContainer, distanceFragment)
            }
            (activity as MainActivity).bottomNavView?.visibility = View.GONE
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

    private fun closePage(fragment: Fragment) {
        binding.fragmentContainer.animate()
            .translationY((binding.fragmentContainer.height).toFloat())
            .setDuration(300)
            .start()

        postDelayed({
            childFragmentManager.commit {
                fragment.let {
                    remove(it)
                    binding.fragmentContainer.isVisible = false
                    hideKeyboardAction()
                    reDrawBars()
                    reDrawPage()
                }
            }
        }, 350)
        (activity as MainActivity).bottomNavView?.visibility = View.VISIBLE
    }

    private fun reDrawPage() {
        binding.fragmentContainer.animate()
            .translationY(0.0f)
            .setDuration(10)
            .start()

        postDelayed({
            binding.fragmentContainer.isVisible = true
        }, 20)
    }
}