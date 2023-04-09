package com.bestDate.presentation.main.userProfile.invitationList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.InvitationCard
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentInvitationBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.main.userProfile.invitationList.adapters.BaseInvitationAdapter

abstract class BaseInvitationFragment(open var navigateAction: (ShortUserData?) -> Unit) :
    BaseVMFragment<FragmentInvitationBinding, InvitationListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInvitationBinding =
        { inflater, parent, attach -> FragmentInvitationBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<InvitationListViewModel> =
        InvitationListViewModel::class.java
    override val statusBarColor = R.color.bg_main
    private var refreshingMode: Boolean = false

    abstract fun getInvitationAdapter(): BaseInvitationAdapter
    abstract fun getNoDataTitle(): Int

    protected lateinit var adapter: BaseInvitationAdapter

    override fun onInit() {
        super.onInit()
        with(binding) {
            noDataView.setTitle(getString(getNoDataTitle()))

            adapter = getInvitationAdapter()
            invitationListView.layoutManager = LinearLayoutManager(requireContext())
            invitationListView.adapter = adapter

            refreshView.setOnRefreshListener {
                adapter.refresh()
            }

            adapter.addLoadStateListener {
                refreshView.isRefreshing = it.source.refresh is LoadState.Loading && refreshingMode
                noDataView.noData =
                    it.source.refresh !is LoadState.Loading && adapter.itemCount == 0
                noDataView.toggleLoading(it.source.refresh is LoadState.Loading && !refreshingMode)
            }
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        adapter.userClick = {
            navigateAction.invoke(it)
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.errorLiveData) {
            binding.refreshView.isRefreshing = false
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }

    fun loadItems(items: PagingData<InvitationCard>) {
        adapter.submitData(lifecycle, items)
    }
}