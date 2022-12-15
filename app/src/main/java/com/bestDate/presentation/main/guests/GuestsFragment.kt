package com.bestDate.presentation.main.guests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentGuestsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuestsFragment : BaseVMFragment<FragmentGuestsBinding, GuestsViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGuestsBinding =
        { inflater, parent, attach -> FragmentGuestsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<GuestsViewModel> = GuestsViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarColor = R.color.bg_main
    private val adapterNew: GuestsAdapter = GuestsAdapter()
    private val adapterPrev: GuestsAdapter = GuestsAdapter()

    override val statusBarLight = false
    override val navBarLight = false

    override fun onInit() {
        super.onInit()
        setUpToolbar()
        setUpGuestsList()
        setUpSwipe()
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.guests)
        binding.toolbar.onProfileClick = {
            findNavController().navigate(R.id.action_global_profile_nav_graph_from_guests)
        }
    }

    private fun setUpGuestsList() {
        binding.run {
            newHeader.root.text = getString(R.string.new_guests)
            recyclerViewGuestsNew.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapterNew.itemClick = {
                    goToAnotherProfile(it?.guest)
                }
                adapter = adapterNew

            }

            prevHeader.root.text = getString(R.string.prev_guests)
            recyclerViewGuestsPrev.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapterPrev.itemClick = {
                    goToAnotherProfile(it?.guest)
                }
                adapter = adapterPrev
            }
        }

        viewModel.getGuests()
    }

    private fun goToAnotherProfile(user: ShortUserData?) {
        navController.navigate(GuestsFragmentDirections.actionGlobalAnotherProfileNavGraph(user))
    }

    private fun setUpSwipe() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.clearData()
            viewModel.refreshUser()
            viewModel.getGuests()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.user.observe(viewLifecycleOwner) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
        }
        viewModel.guestsListNew.observe(viewLifecycleOwner) { guestsList ->
            adapterNew.submitList(guestsList) {
                binding.swipeRefresh.isRefreshing = false
                binding.newHeader.root.isVisible = guestsList.isNotEmpty()
            }
            viewModel.markGuestsViewed(guestsList.map {
                it.id
            }.toMutableList())
        }

        viewModel.guestsListPrev.observe(viewLifecycleOwner) {
            adapterPrev.submitList(it) {
                binding.swipeRefresh.isRefreshing = false
                binding.prevHeader.root.isVisible = it.isNotEmpty()
            }
        }

        viewModel.guestsListIsEmpty.observe(viewLifecycleOwner) {
            if (!it) binding.swipeRefresh.isRefreshing = false
            binding.noDataViewWithLoading.noData = it
        }

        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            if (!binding.swipeRefresh.isRefreshing &&
                viewModel.guestsListIsEmpty.value == true
            ) binding.noDataViewWithLoading.toggleLoading(it)
        }
    }
}