package com.bestDate.presentation.main.duels.top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.model.BackScreenType
import com.bestDate.databinding.FragmentTopBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopFragment : BaseVMFragment<FragmentTopBinding, TopViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTopBinding =
        { inflater, parent, attach -> FragmentTopBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<TopViewModel> = TopViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarColor = R.color.bg_main

    override val statusBarLight = false
    override val navBarLight = false

    private val args by navArgs<TopFragmentArgs>()
    private var adapter = TopAdapter()

    override fun onInit() {
        super.onInit()
        setUpToolbar()
        setUpSelectorView()
        setUpRecyclerView()
        viewModel.gender = args.gender
        getNavigationResult<Boolean>(NavigationResultKey.CHECK_GENDER) {
            if (it) viewModel.getGenderFromUseCase()
            else viewModel.gender = args.gender
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.top_50)
        binding.toolbar.backClick = { goBack() }
        binding.toolbar.lineVisible = false
    }

    private fun setUpSelectorView() {
        binding.selectorView.onClick = {
            viewModel.gender = it
            binding.decoratedFilterButton.gender = it
            viewModel.getTop()
        }
    }

    private fun setUpAdapterClick() {
        adapter.itemClick = {
            findNavController().navigate(
                TopFragmentDirections.actionGlobalTopToAnotherProfile(
                    it?.user,
                    BackScreenType.DUELS
                )
            )
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        setUpAdapterClick()
        binding.recyclerView.adapter = adapter
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()

        observe(viewModel.user) {
            val country = it?.location?.country.orEmpty()
            binding.decoratedFilterButton.country = country
            binding.decoratedFilterButton.gender = viewModel.gender
            viewModel.country = country
            binding.selectorView.setGender(viewModel.gender)
            viewModel.getTop()
        }

        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
        }

        observe(viewModel.topsResults) {
            adapter.items = it ?: mutableListOf()
        }

        observe(viewModel.loadingLiveData) {
            binding.loader.isVisible = it
            binding.recyclerView.isVisible = !it
        }
    }

    override fun goBack() {
        setNavigationResult(NavigationResultKey.GENDER_DUELS, viewModel.gender)
        super.goBack()
    }
}