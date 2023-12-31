package com.bestDate.presentation.main.duels

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.BackScreenType
import com.bestDate.databinding.FragmentDuelsBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.DuelElementView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DuelsFragment : BaseVMFragment<FragmentDuelsBinding, DuelsViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDuelsBinding =
        { inflater, parent, attach -> FragmentDuelsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<DuelsViewModel> = DuelsViewModel::class.java

    override val statusBarColor = R.color.bg_main

    override fun onInit() {
        super.onInit()
        binding.resultView.isVisible = false
        setUpToolbar()
        viewModel.setUserGender()
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.duels)
        binding.toolbar.onProfileClick = {
            navController.navigate(DuelsFragmentDirections.actionGlobalTopToProfile())
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.myDuelsButton.click = {
            navController.navigate(DuelsFragmentDirections.actionDuelsToMyTopDuels())
        }
        binding.topButton.onClick = {
            navController.navigate(
                DuelsFragmentDirections.actionDuelsToTop(viewModel.gender)
            )
        }
        binding.noDataView.onClick = {
            navController.navigate(
                DuelsFragmentDirections.actionDuelsToTop(viewModel.gender)
            )
        }
        binding.resultView.openProfile = {
            navController.navigate(
                DuelsFragmentDirections.actionGlobalTopToAnotherProfile(it, BackScreenType.DUELS)
            )
        }
    }

    private fun setUpFilterButtons() {
        binding.locationFilterButton.label = getString(R.string.universe)
        binding.locationFilterButton.isActive = true
        binding.selectorView.setGender(viewModel.gender)
        binding.selectorView.onClick = {
            viewModel.gender = it
            viewModel.getDuels()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.genderIsSetLiveData) {
            setUpFilterButtons()
        }
        observe(viewModel.duelImages) {
            binding.duelView.isVisible = it?.isEmpty() != true
            binding.noDataView.isVisible = it?.isEmpty() == true
            binding.noDataView.noData = it?.isEmpty() == true
            if (!it.isNullOrEmpty()) {
                setUpElement(binding.firstDuelElementView, it.first(), it.last())
                setUpElement(binding.secondDuelElementView, it.last(), it.first())
            }
        }
        observe(viewModel.user) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
        }
        observe(viewModel.hasNewDuels) {
            binding.myDuelsButton.badgeOn = it
        }
        observe(viewModel.duelResults) {
            binding.resultView.isVisible = it?.isNotEmpty() == true
            binding.resultView.duelProfiles = it
        }
        observe(viewModel.loadingLiveData) {
            if (viewModel.duelImages.value.isNullOrEmpty()) binding.noDataView.toggleLoading(it)
        }
        observe(viewModel.coins) {
            binding.amountCoins.text = it
        }
        observe(viewModel.errorLiveData) {
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }

    private fun setUpElement(
        duelElementView: DuelElementView,
        profileImage: Pair<Bitmap?, Int?>,
        anotherProfileImage: Pair<Bitmap?, Int?>
    ) {
        duelElementView.apply {
            setBitmap(profileImage.first)
            likeClick = {
                viewModel.postVote(profileImage.second.orZero, anotherProfileImage.second.orZero)
            }
        }
    }

    override fun networkIsUpdated() {
        super.networkIsUpdated()
        viewModel.getDuels()
    }
}