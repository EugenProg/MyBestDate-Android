package com.bestDate.presentation.main.userProfile.matchesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentMatchesListBinding
import com.bestDate.view.alerts.showMatchActionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchesListFragment : BaseVMFragment<FragmentMatchesListBinding, MatchesListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMatchesListBinding =
        { inflater, parent, attach -> FragmentMatchesListBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<MatchesListViewModel> = MatchesListViewModel::class.java

    private val args by navArgs<MatchesListFragmentArgs>()
    override val statusBarColor = R.color.bg_main
    private lateinit var adapter: MatchesListAdapter

    override fun onInit() {
        super.onInit()

        adapter = MatchesListAdapter(args.myPhoto)
        binding.matchesListView.layoutManager = LinearLayoutManager(requireContext())
        binding.matchesListView.adapter = adapter

        adapter.itemClick = { item, type ->
            if (type == MatchesSelectType.USER) {
                navController.navigate(
                    MatchesListFragmentDirections.actionGlobalAnotherProfile(item.user))
            } else {
                requireActivity().showMatchActionDialog(item, args.myPhoto, {
                    navController.navigate(
                        MatchesListFragmentDirections.actionGlobalAnotherProfile(it))
                }, {
                    showMessage("open chat")
                })
            }
        }

        binding.refreshView.setOnRefreshListener {
            viewModel.getMatches()
        }

        viewModel.getMatches()
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.toolbar.backClick = {
            goBack()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()

        viewModel.matchesList.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.refreshView.isRefreshing = false
                binding.noDataView.noData = it.isEmpty()
            }
        }
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            if (!binding.refreshView.isRefreshing &&
                viewModel.matchesList.value.isNullOrEmpty()
            ) binding.noDataView.toggleLoading(it)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            binding.refreshView.isRefreshing = false
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }
}