package com.bestDate.presentation.main.duels.top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bestDate.R
import com.bestDate.data.extension.*
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

    //private val args by navArgs<TopFragmentArgs>()
   /* private var adapter = TopAdapter()
    private var getGenderFromLocal = false*/

    override fun onInit() {
        super.onInit()
        loader = LoaderDialog(requireActivity())
        binding.toolbar.backClick = { goBack() }
        setUpSelectorView()
        setUpPager()
        viewModel.getTop()
        /*setUpRecyclerView()
        getNavigationResult<Boolean>(NavigationResultKey.CHECK_GENDER) {
            if (it) {
                viewModel.getTop()
                getGenderFromLocal = true
            }
        }*/
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
        }
        observe(viewModel.loadingMode) {
            if (it) loader.startLoading()
            else loader.stopLoading()
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
    }

    private fun setUpSelectorView() {
        binding.selectorView.onClick = {
            viewModel.gender = it
            binding.decoratedFilterButton.gender = it
            binding.pager.setCurrentItem(it.ordinal, true)
        }
    }

/*    private fun setUpAdapterClick() {
        adapter.itemClick = {
            findNavController().navigate(
                TopFragmentDirections.actionGlobalTopToAnotherProfile(
                    it?.user,
                    BackScreenType.DUELS
                )
            )
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        setUpAdapterClick()
        binding.recyclerView.adapter = adapter
    }*/

/*    override fun onViewLifecycle() {
        super.onViewLifecycle()

        observe(viewModel.user) {
            if (!getGenderFromLocal) {
                viewModel.getTop(args.gender)
            }
        }

        observe(viewModel.gender) {
            binding.decoratedFilterButton.gender = it
            binding.selectorView.setGender(it)
        }

        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
        }

        observe(viewModel.topsResults) {
            adapter.items = it ?: mutableListOf()
        }

        observe(viewModel.loadingLiveData) {
            binding.loader.isVisible = it
            binding.recyclerView.isVisible = !it
        }
    }

    override fun goBack() {
        setNavigationResult(NavigationResultKey.GENDER_DUELS, viewModel.gender.value)
        super.goBack()
    }*/
}