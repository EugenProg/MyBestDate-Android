package com.bestDate.presentation.main.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.isToLastPositionScrolled
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.swipeDeleteListener
import com.bestDate.data.model.BackScreenType
import com.bestDate.databinding.FragmentChatListBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.alerts.LoaderDialog
import com.bestDate.view.alerts.showDeleteDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : BaseVMFragment<FragmentChatListBinding, ChatListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChatListBinding =
        { inflater, parent, attach -> FragmentChatListBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<ChatListViewModel> = ChatListViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private lateinit var loader: LoaderDialog
    private lateinit var adapter: ChatListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onInit() {
        super.onInit()

        loader = LoaderDialog(requireActivity())
        adapter = ChatListAdapter()
        with(binding) {
            toolbar.title = getString(R.string.chats)

            layoutManager = LinearLayoutManager(requireContext())
            chatListView.layoutManager = layoutManager
            chatListView.adapter = adapter

            chatListView.swipeDeleteListener {
                val item = viewModel.chatList.value?.get(it)
                requireActivity().showDeleteDialog(
                    getString(R.string.this_chat_will_be_deleted, item?.user?.name.orEmpty()),
                    closeClick = {
                        adapter.notifyItemChanged(it)
                    }
                ) {
                    viewModel.deleteChat(item?.id)
                }
            }
        }
        binding.loader.isVisible = viewModel.chatList.value.isNullOrEmpty()
        viewModel.refreshChatList()
        checkNextListNeeding()
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.toolbar.onProfileClick = {
            navController.navigate(ChatListFragmentDirections.actionGlobalChatListToProfile())
        }

        adapter.clickAction = {
            navController.navigate(
                ChatListFragmentDirections
                    .actionGlobalChatListToChat(it.user, BackScreenType.CHAT_LIST)
            )
        }

        adapter.loadMoreItems = {
            viewModel.loadNextPage()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.user) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
        }
        observe(viewModel.chatList) {
            adapter.submitList(it) {
                adapter.meta = viewModel.meta
                loader.stopLoading()
                binding.loader.isVisible = false
            }
        }
        observe(viewModel.deleteLiveData) {
            loader.toggleLoading(it)
        }
        observe(viewModel.errorLiveData) {
            loader.stopLoading()
            showMessage(it.exception.message)
        }
    }

    private fun checkNextListNeeding() {
        binding.chatListView.isToLastPositionScrolled {
            if (viewModel.meta.current_page.orZero < viewModel.meta.last_page.orZero && !adapter.loadingMode) {
                adapter.loadingMode = true
                viewModel.loadNextPage()
            }
        }
    }

    override fun networkIsUpdated() {
        super.networkIsUpdated()
        viewModel.refreshChatList()
    }
}