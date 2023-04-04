package com.bestDate.presentation.main.duels.top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.onPageChanged
import com.bestDate.databinding.FragmentTopListBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.main.userProfile.invitationList.adapters.ViewPagerAdapter
import com.bestDate.presentation.registration.Gender
import com.bestDate.view.alerts.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopListFragment : BaseVMFragment<FragmentTopListBinding, TopViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTopListBinding =
        { inflater, parent, attach -> FragmentTopListBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<TopViewModel> = TopViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private lateinit var loader: LoaderDialog

    override fun onInit() {
        super.onInit()
        loader = LoaderDialog(requireActivity())
        binding.toolbar.backClick = { goBack() }
        setUpPager()
        setUpSelectorView()
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
        }
    }

    private fun setUpPager() {
        val fragments = mutableListOf<Fragment>(
            TopWomanFragment(),
            TopManFragment()
        )
        val adapter =
            ViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)
        with(binding) {
            pager.adapter = adapter

            pager.onPageChanged {
                when (it) {
                    0 -> {
                        binding.selectorView.setGender(Gender.WOMAN)
                        viewModel.gender = Gender.WOMAN
                        binding.decoratedFilterButton.gender = Gender.WOMAN
                    }
                    1 -> {
                        binding.selectorView.setGender(Gender.MAN)
                        viewModel.gender = Gender.MAN
                        binding.decoratedFilterButton.gender = Gender.MAN
                    }
                }
            }
        }

        binding.pager.setCurrentItem(viewModel.gender.ordinal, false)
    }

    private fun setUpSelectorView() {
        binding.selectorView.setGender(viewModel.gender)
        binding.selectorView.onClick = {
            viewModel.gender = it
            binding.decoratedFilterButton.gender = it
            binding.pager.setCurrentItem(it.ordinal, true)
        }
    }
}