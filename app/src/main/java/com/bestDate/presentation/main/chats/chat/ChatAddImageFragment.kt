package com.bestDate.presentation.main.chats.chat

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.data.extension.getBitmap
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.scale
import com.bestDate.data.model.Image
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentChatAddImageBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.alerts.showBanningMessagesDialog
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatAddImageFragment(
    private val user: ShortUserData?,
    private val image: Image?
) : BaseVMFragment<FragmentChatAddImageBinding, ChatViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChatAddImageBinding =
        { inflater, parent, attach ->
            FragmentChatAddImageBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<ChatViewModel> = ChatViewModel::class.java

    override val statusBarColor: Int = R.color.bg_main
    private var imageLiveData: MutableLiveData<Pair<String?, Bitmap>> = MutableLiveData()
    var closeAction: (() -> Unit)? = null
    var navigateToTariffList: (() -> Unit)? = null

    override fun onInit() {
        super.onInit()
        binding.toolbar.title = user?.name
        Glide.with(requireContext())
            .load(image?.uri)
            .into(binding.imageView)
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            toolbar.backClick = {
                goBack()
            }
            inputView.sendClick = { text ->
                if (viewModel.messageSendAllowed()) {
                    val bitmap = image?.uri.getBitmap(requireContext())
                    bitmap?.scale(1024.0)?.let { imageLiveData.postValue(Pair(text, it)) }
                } else {
                    binding.inputView.toggleSendLoading(false)
                    requireActivity().showBanningMessagesDialog {
                        navigateToTariffList?.invoke()
                    }
                }
            }
            inputView.translateClick = {
                viewModel.translateText(it, user?.language)
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(imageLiveData) {
            viewModel.sendImageMessage(user?.id, it.first, it.second)
        }
        observe(viewModel.sendMessageLiveData) {
            binding.inputView.toggleSendLoading(false)
            binding.inputView.clearInput()
            closeAction?.invoke()
        }
        observe(viewModel.translateLiveData) {
            binding.inputView.setTranslatedText(it)
            binding.inputView.toggleTranslateLoading(false)
        }
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
            binding.inputView.toggleSendLoading(false)
            binding.inputView.toggleTranslateLoading(false)
        }
    }

    override var customBackNavigation: Boolean = true

    override fun goBack() {
        closeAction?.invoke()
    }
}