package com.bestDate.presentation.main.guests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.Guest
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentGuestsBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuestsFragment : BaseVMFragment<FragmentGuestsBinding, GuestsViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGuestsBinding =
        { inflater, parent, attach -> FragmentGuestsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<GuestsViewModel> = GuestsViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private val adapter: GuestsAdapter = GuestsAdapter()

    override fun onInit() {
        super.onInit()
        setUpToolbar()
        setUpGuestsList()
        setUpSwipe()
        viewModel.getGuests()
        binding.noDataViewWithLoading.toggleLoading(true)
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.guests)
        binding.toolbar.onProfileClick = {
            navController.navigate(R.id.action_global_guests_to_profile)
        }
    }

    private fun setUpGuestsList() {
        with(binding) {
            guestsView.layoutManager = LinearLayoutManager(requireContext())
            guestsView.adapter = adapter

            adapter.itemClick = {
                goToAnotherProfile(it?.guest)
            }
            adapter.loadMoreItems = {
                viewModel.loadNextPage()
            }
        }
    }

    private fun goToAnotherProfile(user: ShortUserData?) {
        navController.navigate(
            GuestsFragmentDirections
                .actionGlobalGuestsToAnotherProfile(user, BackScreenType.GUESTS)
        )
    }

    private fun setUpSwipe() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getGuests()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.user) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
        }
        observe(viewModel.guestsList) {
            adapter.submitList(it) {
                adapter.meta = viewModel.meta
                binding.swipeRefresh.isRefreshing = false
                binding.noDataViewWithLoading.toggleLoading(false)
                binding.noDataViewWithLoading.noData = it.isNullOrEmpty()
                markAsViewed(it)
            }
        }

        observe(viewModel.errorLiveData) {
            binding.swipeRefresh.isRefreshing = false
            binding.noDataViewWithLoading.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }

    private fun markAsViewed(it: MutableList<Guest>?) {
        if (!it.isNullOrEmpty()) {
            val list = it.filter { guest -> guest.viewed != true }
                .map { guest -> guest.id }
                .toMutableList()
            if (list.isNotEmpty()) viewModel.markGuestsViewed(list)
        }
    }
}