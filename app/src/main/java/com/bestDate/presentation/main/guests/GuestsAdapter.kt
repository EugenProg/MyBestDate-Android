package com.bestDate.presentation.main.guests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.R
import com.bestDate.base.BaseClickViewHolder
import com.bestDate.base.BaseViewHolder
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.Guest
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemGuestBinding
import com.bestDate.databinding.ItemHeaderBinding
import com.bestDate.databinding.ItemLoadingSearchElementBinding
import com.bestDate.databinding.ItemSearchProfilesElementBinding
import com.bumptech.glide.Glide

const val HEADER = 0
const val ITEM = 1

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
        override fun bindView(item: Guest?, itemClick: ((Guest?) -> Unit)?) {
            binding.run {
                nameTextView.text = item?.guest?.name
                locationTextView.text =
                    "${item?.guest?.location?.city}, ${item?.guest?.location?.country}"
                ageTextView.text = item?.guest?.getAge()
                Glide.with(binding.root.context)
                    .load(item?.guest?.main_photo?.thumb_url)
                    .placeholder(R.drawable.ic_default_photo)
                    .into(binding.profileImageView)

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