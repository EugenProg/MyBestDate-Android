package com.bestDate.presentation.main.duels.top

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.DuelProfile
import com.bestDate.databinding.ItemTopProfilesElementBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bumptech.glide.Glide
import java.math.BigDecimal
import java.math.RoundingMode

class TopAdapter : PagingDataAdapter<DuelProfile, TopAdapter.TopListViewHolder>(TopListDiffUtil()) {

    var itemClick: ((DuelProfile?) -> Unit)? = null

    class TopListDiffUtil : DiffUtil.ItemCallback<DuelProfile>() {
        override fun areItemsTheSame(oldItem: DuelProfile, newItem: DuelProfile): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DuelProfile, newItem: DuelProfile): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopListViewHolder {
        return TopListViewHolder(
            ItemTopProfilesElementBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TopListViewHolder, position: Int) {
        holder.bind(getItem(position), itemClick)
    }

    class TopListViewHolder(
        override val binding: ItemTopProfilesElementBinding
    ) : BaseClickViewHolder<DuelProfile?, ((DuelProfile?) -> Unit)?, ItemTopProfilesElementBinding>(
        binding
    ) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: DuelProfile?, itemClick: ((DuelProfile?) -> Unit)?) {
            binding.run {
                val color = when (absoluteAdapterPosition) {
                    0 -> R.color.gold
                    1 -> R.color.silver
                    2 -> R.color.bronze
                    else -> R.color.black
                }

                val colorAward = when (absoluteAdapterPosition) {
                    0 -> R.color.gold
                    1 -> R.color.silver
                    2 -> R.color.bronze
                    else -> R.color.white
                }
                starsStatus.setColorFilter(
                    ContextCompat.getColor(itemView.context, color),
                    android.graphics.PorterDuff.Mode.SRC_IN
                );
                awardStatus.setColorFilter(
                    ContextCompat.getColor(itemView.context, colorAward),
                    android.graphics.PorterDuff.Mode.SRC_IN
                );
                placeStatus.text = "№${absoluteAdapterPosition + 1}"
                percentNumber.text =
                    (item?.rating?.setScale(1, RoundingMode.UP) ?: BigDecimal.ZERO).toString()
                Glide.with(itemView.context)
                    .load(item?.thumb_url)
                    .placeholder(R.drawable.ic_default_photo)
                    .into(binding.profileImageView)
                root.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
            }
        }
    }
}