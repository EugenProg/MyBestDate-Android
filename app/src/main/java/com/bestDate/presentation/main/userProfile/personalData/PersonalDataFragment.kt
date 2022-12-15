package com.bestDate.presentation.main.userProfile.personalData

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentPersonalDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalDataFragment : BaseVMFragment<FragmentPersonalDataBinding, PersonalDataViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPersonalDataBinding =
        { inflater, parent, attach ->
            FragmentPersonalDataBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<PersonalDataViewModel> = PersonalDataViewModel::class.java

    override val statusBarColor = R.color.bg_main

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            toolbar.backClick = {
                goBack()
            }
        }
    }
}