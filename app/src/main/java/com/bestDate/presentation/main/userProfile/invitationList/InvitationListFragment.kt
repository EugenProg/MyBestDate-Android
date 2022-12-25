package com.bestDate.presentation.main.userProfile.invitationList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bestDate.R
import com.bestDate.presentation.base.BaseFragment
import com.bestDate.data.extension.onPageChanged
import com.bestDate.databinding.FragmentInvitationListBinding
import com.bestDate.presentation.main.userProfile.invitationList.adapters.ViewPagerAdapter
import com.bestDate.view.button.InvitationActions

class InvitationListFragment : BaseFragment<FragmentInvitationListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInvitationListBinding =
        { inflater, parent, attach ->
            FragmentInvitationListBinding.inflate(
                inflater,
                parent,
                attach
            )
        }

    override val statusBarColor = R.color.bg_main

    override fun onInit() {
        super.onInit()
        with(binding) {
            val fragments = mutableListOf<Fragment>(
                NewInvitationFragment(), AnsweredInvitationFragment(), SentInvitationFragment()
            )
            val adapter =
                ViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)

            pager.adapter = adapter

            pager.onPageChanged {
                when (it) {
                    0 -> buttonsBox.setNewBtn()
                    1 -> buttonsBox.setAnsweredBtn()
                    2 -> buttonsBox.setSentBtn()
                }
            }

            buttonsBox.onClick = {
                when (it) {
                    InvitationActions.NEW -> pager.setCurrentItem(0, true)
                    InvitationActions.ANSWERED -> pager.setCurrentItem(1, true)
                    InvitationActions.SENT -> pager.setCurrentItem(2, true)
                }
            }
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            toolbar.backClick = {
                goBack()
            }
        }
    }
}