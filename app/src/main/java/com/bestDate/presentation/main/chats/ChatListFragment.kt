package com.bestDate.presentation.main.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.swipeDeleteListener
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

    override fun onInit() {
        super.onInit()

        loader = LoaderDialog(requireActivity())
        adapter = ChatListAdapter()
        with(binding) {
            toolbar.title = getString(R.string.chats)

            chatListView.layoutManager = LinearLayoutManager(requireContext())
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
        viewModel.refreshChatList()
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.toolbar.onProfileClick = {
            navController.navigate(ChatListFragmentDirections.actionGlobalProfileNavGraphFromChats())
        }

        adapter.clickAction = {
            showMessage(it.user?.name.orEmpty())
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.user.observe(viewLifecycleOwner) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
        }
        viewModel.chatList.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                loader.stopLoading()
            }
        }
        viewModel.deleteLiveData.observe(viewLifecycleOwner) {
            loader.toggleLoading(it)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            loader.stopLoading()
            showMessage(it.exception.message)
        }
    }
}