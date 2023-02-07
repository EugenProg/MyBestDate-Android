package com.bestDate.view.alerts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bestDate.data.extension.*
import com.bestDate.databinding.DialogCreateInvitationBinding
import com.bestDate.databinding.ItemCardInvitationBinding
import com.bestDate.db.entity.Invitation

fun FragmentActivity.showCreateInvitationDialog(invitationList: MutableList<Invitation>,
                                                itemClick: (Invitation) -> Unit) {
    val binding = DialogCreateInvitationBinding.inflate(layoutInflater)

    val dialog = getDialog(binding.root)

    with(binding) {
        box.showWithSlideTopAndRotateAnimation()
        rotateBtn.onSafeClick = {
            box.rotateHorizontally {
                frontBox.isVisible = false
                backBox.isVisible = true
            }
        }

        actionsList.layoutManager = LinearLayoutManager(this@showCreateInvitationDialog)
        actionsList.adapter = InvitationActionsListAdapter(invitationList) {
            itemClick.invoke(it)
            dialog.closeWithAnimation(root, owner = this@showCreateInvitationDialog)
        }

        root.setOnSaveClickListener {
            dialog.closeWithAnimation(root, owner = this@showCreateInvitationDialog)
        }
    }
}

class InvitationActionsListAdapter(private val items: MutableList<Invitation>,
                                   private val click: (Invitation) -> Unit):
    RecyclerView.Adapter<InvitationActionsListAdapter.InvitationViewHolder>() {

        class InvitationViewHolder(override val binding: ItemCardInvitationBinding):
            BaseClickViewHolder<Invitation, (Invitation) -> Unit, ItemCardInvitationBinding>(binding) {
            override fun bindView(item: Invitation, itemClick: (Invitation) -> Unit) {
                binding.title.text = item.name

                binding.root.setOnSaveClickListener {
                    itemClick.invoke(item)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InvitationViewHolder(
        ItemCardInvitationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: InvitationViewHolder, position: Int) {
        holder.bindView(items[position], click)
    }

    override fun getItemCount() = items.size
}