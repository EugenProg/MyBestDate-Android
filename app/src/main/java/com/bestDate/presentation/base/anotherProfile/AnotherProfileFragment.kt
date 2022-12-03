package com.bestDate.presentation.base.anotherProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.model.ShortUserData
import com.bestDate.data.utils.Logger
import com.bestDate.databinding.FragmentAnotherProfileBinding
import dagger.hilt.android.AndroidEntryPoint

abstract class AnotherProfileFragment :
    BaseVMFragment<FragmentAnotherProfileBinding, AnotherProfileViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAnotherProfileBinding =
        { inflater, parent, attach ->
            FragmentAnotherProfileBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<AnotherProfileViewModel> = AnotherProfileViewModel::class.java

    protected var user: ShortUserData? = null

    abstract fun getUserInfo(): ShortUserData?

    override fun onInit() {
        super.onInit()
        makeStatusBarTransparent(binding.header.getTopBoxView())
        user = getUserInfo()
        viewModel.getUserById(user?.id)
        with(binding.header) {
            setImage(user?.main_photo)
            setDistance(user?.distance)
            setZodiac(user?.birthday)
            isOnline(user?.is_online)
            isBlocked(user?.blocked_me)

            clickBack = {
                navController.popBackStack()
            }
        }
    }

    private fun setBackground(isBlocked: Boolean?) {
        if (isBlocked == true) binding.root.setBackgroundResource(R.drawable.bg_blocked_profile)
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.user.observe(viewLifecycleOwner) {
            with(binding.header) {
                isOnline(it?.is_online)
                isBlocked(it?.blocked_me)
                setBackground(it?.blocked_me)
            }
        }
    }
}