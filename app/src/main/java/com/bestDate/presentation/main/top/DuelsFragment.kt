package com.bestDate.presentation.main.top

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.FragmentDuelsBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.registration.Gender
import com.bestDate.view.DuelElementView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DuelsFragment : BaseVMFragment<FragmentDuelsBinding, TopViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDuelsBinding =
        { inflater, parent, attach -> FragmentDuelsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<TopViewModel> = TopViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarColor = R.color.bg_main

    override val statusBarLight = false
    override val navBarLight = false
    private var gender: Gender = Gender.MAN

    override fun onInit() {
        super.onInit()
        binding.resultView.visibility = View.INVISIBLE
        binding.noDataView.setTitle(R.string.duels_are_over)
        binding.noDataView.setDesc(R.string.you_voted_for_all_the_photos)
        binding.noDataView.setDirectionsText(R.string.but_you_can_also_go_to_the_top)
        setUpToolbar()
        setUpFilterButtons()
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.duels)
        binding.toolbar.onProfileClick = {
            navController.navigate(R.id.action_global_profile_nav_graph_from_top)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.myDuelsButton.click = {
            navController.navigate(DuelsFragmentDirections.actionProfileToMyTopDuels())
        }
    }

    private fun setUpFilterButtons() {
        binding.locationFilterButton.label = getString(R.string.universe)
        binding.locationFilterButton.isActive = true
        binding.selectorView.onClick = {
            gender = it
            reload()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        reload()

        viewModel.duelsLiveData.observe(viewLifecycleOwner) {
            binding.duelView.isVisible = it?.isEmpty() != true
            binding.noDataView.isVisible = it?.isEmpty() == true
            if (!it.isNullOrEmpty()) {
                setUpElement(binding.firstDuelElementView, it.first(), it[1])
                setUpElement(binding.secondDuelElementView, it[1], it.first())
            }
        }
        viewModel.user.observe(viewLifecycleOwner) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
            binding.selectorView.lookFor = it?.look_for?.first()
            gender =
                if (it?.look_for?.first() == getString(Gender.MAN.gender)) Gender.MAN else Gender.WOMAN
        }
        viewModel.duelsResultLiveData.observe(viewLifecycleOwner) {
            binding.resultView.visibility = View.VISIBLE
            binding.resultView.duelProfiles = it
            reload()
        }
    }

    private fun setUpElement(
        duelElementView: DuelElementView,
        profileImage: ProfileImage?,
        anotherProfileImage: ProfileImage?
    ) {
        duelElementView.apply {
            image = profileImage?.full_url
            photoId = profileImage?.id ?: 0
            likeClick = { winningId ->
                viewModel.postVote(winningId, anotherProfileImage?.id ?: 0)
            }
        }
    }

    private fun reload() {
        viewModel.getDuels(
            getString(gender.gender).lowercase(),
            null
            //binding.locationFilterButton.label.lowercase()
        )
    }
}