package com.bestDate.presentation.main.userProfile.myDuels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.MyDuel
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemMyDuelBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bumptech.glide.Glide

class MyDuelsAdapter: PagingDataAdapter<MyDuel, MyDuelsAdapter.MyDuelsViewHolder>(MyDuelDiffUtils()) {

    var itemClick: ((ShortUserData?) -> Unit)? = null

    class MyDuelDiffUtils: DiffUtil.ItemCallback<MyDuel>() {
        override fun areItemsTheSame(oldItem: MyDuel, newItem: MyDuel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MyDuel, newItem: MyDuel): Boolean {
            return oldItem == newItem
        }
    }

    class MyDuelsViewHolder(override val binding: ItemMyDuelBinding):
        BaseClickViewHolder<MyDuel?, ((ShortUserData?) -> Unit)?, ItemMyDuelBinding>(binding) {
        override fun bindView(item: MyDuel?, itemClick: ((ShortUserData?) -> Unit)?) {
            with(binding) {
                Glide.with(itemView.context).load(item?.winning_photo?.thumb_url).centerCrop().into(winnerImage)
                Glide.with(itemView.context).load(item?.loser_photo?.thumb_url).centerCrop().into(loserImage)
                Glide.with(itemView.context).load(item?.voter?.getMainPhoto()?.thumb_url).centerCrop().into(voterImage)

                name.text = item?.voter?.name
                age.text = item?.voter?.getAge()
                location.text = item?.voter?.getLocation()
                time.text = itemView.context.getString(R.string.you_were_voted_for,
                    item?.getVisitPeriod(itemView.context))
                verifyView.isVerified = item?.voter?.full_questionnaire

                voterBox.setOnSaveClickListener {
                    itemClick?.invoke(item?.voter)
                }
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