package com.bestDate.presentation.main.chats.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ChatImage
import com.bestDate.databinding.FragmentChatImageViewBinding
import com.bestDate.presentation.base.BaseFragment
import com.bumptech.glide.Glide

class ChatImageViewFragment(private val image: ChatImage?): BaseFragment<FragmentChatImageViewBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChatImageViewBinding =
        { inflater, parent, attach -> FragmentChatImageViewBinding.inflate(inflater, parent, attach) }

    override val statusBarColor: Int = R.color.bg_main
    var closeAction: (() -> Unit)? = null

    override fun onInit() {
        super.onInit()
        Glide.with(requireContext())
            .load(image?.thumb_url)
            .into(binding.thumbImageView)

        Glide.with(requireContext())
            .load(image?.full_url)
            .into(binding.imageView)
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.backButton.onClick = {
            goBack()
        }
        binding.imageView.setOnSaveClickListener {
            goBack()
        }
    }

    override var customBackNavigation: Boolean = true

    override fun goBack() {
        closeAction?.invoke()
    }
}