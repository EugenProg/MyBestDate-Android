package com.bestDate.presentation.main.guests

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.R
import com.bestDate.data.extension.getVisitPeriod
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.Guest
import com.bestDate.databinding.ItemGuestBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bumptech.glide.Glide

class GuestsAdapter : ListAdapter<Guest, RecyclerView.ViewHolder>(GuestsDiffUtils()) {

    var itemClick: ((Guest?) -> Unit)? = null

    class GuestsDiffUtils : DiffUtil.ItemCallback<Guest>() {
        override fun areItemsTheSame(oldItem: Guest, newItem: Guest): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Guest, newItem: Guest): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GuestViewHolder(
            ItemGuestBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GuestViewHolder) holder.bind(getItem(position), itemClick)
    }

    class GuestViewHolder(
        override val binding: ItemGuestBinding
    ) : BaseClickViewHolder<Guest?, ((Guest?) -> Unit)?, ItemGuestBinding>(
        binding
    ) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: Guest?, itemClick: ((Guest?) -> Unit)?) {
            binding.run {
                newVisitTextView.isVisible = item?.viewed == false
                nameTextView.text = item?.guest?.name
                timeTextView.text = item?.visit_at.getVisitPeriod(binding.root.context)
                locationTextView.text =
                    "${item?.guest?.location?.city}, ${item?.guest?.location?.country}"
                ageTextView.text = item?.guest?.getAge()
                Glide.with(binding.root.context)
                    .load(item?.guest?.main_photo?.thumb_url)
                    .placeholder(R.drawable.ic_default_photo)
                    .into(binding.profileImageView)
                verifyView.isVerified = item?.guest?.full_questionnaire

                root.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
            }
        }
    }

    override fun getItem(position: Int): Guest? {
        return currentList[position]
    }

}