package com.bestDate.presentation.main.top

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.FragmentTopBinding
import com.bestDate.presentation.registration.Gender
import com.bestDate.view.DuelElementView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopFragment : BaseVMFragment<FragmentTopBinding, TopViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTopBinding =
        { inflater, parent, attach -> FragmentTopBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<TopViewModel> = TopViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarColor = R.color.bg_main

    override val statusBarLight = false
    override val navBarLight = false
    private var gender: Gender = Gender.MAN

    override fun onInit() {
        super.onInit()
        binding.resultView.visibility = View.INVISIBLE
        setUpToolbar()
        setUpFilterButtons()
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.top_50)
        binding.toolbar.onProfileClick = {
            findNavController().navigate(R.id.action_global_profile_nav_graph_from_top)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.myDuelsButton.click = {
            navController.navigate(TopFragmentDirections.actionProfileToMyTopDuels())
        }
    }

    private fun setUpFilterButtons() {
        binding.locationFilterButton.label = getString(R.string.universe)
        binding.locationFilterButton.isActive = true

        binding.manSelector.isMan = true
        binding.womanSelector.isMan = false
        binding.manSelector.onClick = {
            binding.womanSelector.isActive = false
            gender = Gender.MAN
            reload()
        }
        binding.womanSelector.onClick = {
            binding.manSelector.isActive = false
            gender = Gender.WOMAN
            reload()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        reload()

        viewModel.duelsLiveData.observe(viewLifecycleOwner) {
            binding.scrollView.isVisible = it?.isEmpty() == true

            setUpElement(binding.firstDuelElementView, it?.first(), it?.get(1))
            setUpElement(binding.secondDuelElementView, it?.get(1), it?.first())
        }
        viewModel.user.observe(viewLifecycleOwner) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
            binding.manSelector.isActive = it?.look_for?.first() == getString(Gender.MAN.gender)
            binding.womanSelector.isActive = it?.look_for?.first() == getString(Gender.WOMAN.gender)
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