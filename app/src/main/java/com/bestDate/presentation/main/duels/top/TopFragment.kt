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

    override val statusBarColor = R.color.bg_main

    private val args by navArgs<TopFragmentArgs>()
    private var adapter = TopAdapter()
    private var getGenderFromLocal = false

    override fun onInit() {
        super.onInit()
        setUpToolbar()
        setUpSelectorView()
        setUpRecyclerView()
        getNavigationResult<Boolean>(NavigationResultKey.CHECK_GENDER) {
            if (it) {
                viewModel.getTop()
                getGenderFromLocal = true
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.top_50)
        binding.toolbar.backClick = { goBack() }
        binding.toolbar.lineVisible = false
    }

    private fun setUpSelectorView() {
        binding.selectorView.onClick = {
            viewModel.getTop(it)
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
            if (!getGenderFromLocal) {
                viewModel.getTop(args.gender)
            }
        }

        observe(viewModel.gender) {
            binding.decoratedFilterButton.gender = it
            binding.selectorView.setGender(it)
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
        setNavigationResult(NavigationResultKey.GENDER_DUELS, viewModel.gender.value)
        super.goBack()
    }
}