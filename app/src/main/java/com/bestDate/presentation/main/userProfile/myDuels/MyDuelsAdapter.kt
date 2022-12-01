package com.bestDate.presentation.main.userProfile.myDuels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.base.BaseClickViewHolder
import com.bestDate.data.model.MyDuel
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemMyDuelBinding
import com.bumptech.glide.Glide

class MyDuelsAdapter: ListAdapter<MyDuel, MyDuelsAdapter.MyDuelsViewHolder>(MyDuelDiffUtils()) {

    var itemClick: ((ShortUserData) -> Unit)? = null

    class MyDuelDiffUtils: DiffUtil.ItemCallback<MyDuel>() {
        override fun areItemsTheSame(oldItem: MyDuel, newItem: MyDuel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MyDuel, newItem: MyDuel): Boolean {
            return oldItem == newItem
        }
    }

    class MyDuelsViewHolder(override val binding: ItemMyDuelBinding):
        BaseClickViewHolder<MyDuel, ((ShortUserData) -> Unit)?, ItemMyDuelBinding>(binding) {
        override fun bindView(item: MyDuel, itemClick: ((ShortUserData) -> Unit)?) {
            with(binding) {
                Glide.with(itemView.context).load(item.winning_photo?.thumb_url).into(winnerImage)
                Glide.with(itemView.context).load(item.loser_photo?.thumb_url).into(loserImage)
                Glide.with(itemView.context).load(item.voter?.main_photo?.thumb_url).into(voterImage)

                name.text = item.voter?.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDuelsViewHolder {
        return MyDuelsViewHolder(
            ItemMyDuelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyDuelsViewHolder, position: Int) {
        holder.bindView(getItem(position), itemClick)
    }
}