package com.bestDate.presentation.main.matches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.toPx
import com.bestDate.data.model.BackScreenType
import com.bestDate.databinding.FragmentMatchesBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.alerts.LoaderDialog
import com.bestDate.view.alerts.showMatchActionDialog
import com.bestDate.view.match.MatchView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchesFragment : BaseVMFragment<FragmentMatchesBinding, MatchesViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMatchesBinding =
        { inflater, parent, attach -> FragmentMatchesBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<MatchesViewModel> = MatchesViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private var mainPhoto: String = ""
    private lateinit var loaderDialog: LoaderDialog
    private lateinit var matchView: MatchView

    override fun onInit() {
        super.onInit()
        loaderDialog = LoaderDialog(requireActivity())
        setUpToolbar()
        setUpMatchesView()
        viewModel.getMatches()
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.matches)
        binding.toolbar.onProfileClick = {
            navController.navigate(MatchesFragmentDirections.actionGlobalMatchesToProfile())
        }
    }

    private fun setUpMatchesView() {
        val needingHeight = resources.displayMetrics.heightPixels - 97f.toPx() - 80f.toPx() -
                resources.displayMetrics.widthPixels - 132f.toPx()
        binding.matchView.isVisible = needingHeight > -8f
        binding.scrollMatchContainer.isVisible = needingHeight < -8f
        matchView = if (needingHeight > -8) binding.matchView else binding.scrollMatchView
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.matchesListButton.click = {
            navController.navigate(MatchesFragmentDirections.actionMatchesToMatchesList())
        }
        matchView.matchAction = {
            viewModel.matchAction(it)
        }
        matchView.nextUser = {
            viewModel.nextUser()
            binding.noDataView.noData = it
            matchView.isVisible = !it
        }
        matchView.openQuestionnaireClick = {
            loaderDialog.startLoading()
            viewModel.getUserById(it)
        }
        matchView.clickAction = {
            navController.navigate(
                MatchesFragmentDirections
                    .actionGlobalMatchesToAnotherProfile(it, BackScreenType.MATCHES)
            )
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.user) {
            mainPhoto = it?.getMainPhotoThumbUrl().orEmpty()
            binding.matchesListButton.badgeOn = it?.new_matches.orZero > 0
            binding.toolbar.photo = mainPhoto
        }
        observe(viewModel.matchesList) {
            binding.noDataView.noData = it.isEmpty()
            matchView.isVisible = it.isNotEmpty()
            matchView.setMatches(it)
        }
        observe(viewModel.matchAction) {
            it?.let { match ->
                viewModel.clearMatch()
                requireActivity().showMatchActionDialog(match, mainPhoto, { user ->
                    navController.navigate(
                        MatchesFragmentDirections
                            .actionGlobalMatchesToAnotherProfile(user, BackScreenType.MATCHES)
                    )
                }, { user ->
                    navController.navigate(
                        MatchesFragmentDirections.actionGlobalMatchesToChat(user, BackScreenType.MATCHES)
                    )
                })
            }
        }
        observe(viewModel.getUserLiveData) {
            loaderDialog.stopLoading()
            navController.navigate(MatchesFragmentDirections.actionMatchesToMatchesQuestionnaire())
        }
        observe(viewModel.loadingMode) {
            binding.noDataView.toggleLoading(it)
        }
        observe(viewModel.errorLiveData) {
            loaderDialog.stopLoading()
            showMessage(it.exception.message)
        }
    }
}