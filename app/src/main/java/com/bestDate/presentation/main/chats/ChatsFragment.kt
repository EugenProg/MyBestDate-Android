package com.bestDate.presentation.main.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentChatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : BaseVMFragment<FragmentChatsBinding, ChatsViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChatsBinding =
        { inflater, parent, attach -> FragmentChatsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<ChatsViewModel> = ChatsViewModel::class.java

    override val navBarColor = R.color.main_dark
    override val statusBarColor= R.color.main_dark

    override val statusBarLight = false
    override val navBarLight = false

    override fun onInit() {
        super.onInit()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.chats)
    }
}