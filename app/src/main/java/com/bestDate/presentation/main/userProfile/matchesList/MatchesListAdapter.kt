package com.bestDate.presentation.main.userProfile.matchesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.Match
import com.bestDate.databinding.ItemMatchesListBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bumptech.glide.Glide

class MatchesListAdapter(private val myPhotoUrl: String) :
    PagingDataAdapter<Match, MatchesListAdapter.MatchesListViewHolder>(MatchesDiffUtils()) {

    var itemClick: ((Match, MatchesSelectType) -> Unit)? = null

    class MatchesDiffUtils : DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem == newItem
        }
    }

    class MatchesListViewHolder(
        override val binding: ItemMatchesListBinding,
        private val myPhotoUrl: String
    ) :
        BaseClickViewHolder<Match?, ((Match, MatchesSelectType) -> Unit)?, ItemMatchesListBinding>(
            binding
        ) {
        override fun bindView(item: Match?, itemClick: ((Match, MatchesSelectType) -> Unit)?) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item?.user?.main_photo?.thumb_url)
                    .circleCrop()
                    .into(avatar)
                Glide.with(itemView.context)
                    .load(myPhotoUrl)
                    .centerCrop()
                    .into(myPhoto)
                Glide.with(itemView.context)
                    .load(item?.user?.main_photo?.thumb_url)
                    .centerCrop()
                    .into(userPhoto)
                name.text = item?.user?.name
                location.text = item?.user?.getLocation()
                time.text = item?.getTime()
                verifyView.isVerified = item?.user?.full_questionnaire

                item?.let { match ->
                    root.setOnSaveClickListener {
                        itemClick?.invoke(match, MatchesSelectType.USER)
                    }

                    matchesBox.setOnSaveClickListener {
                        itemClick?.invoke(match, MatchesSelectType.MATCH)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesListViewHolder {
        return MatchesListViewHolder(
            ItemMatchesListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            myPhotoUrl
        )
    }

    override fun onBindViewHolder(holder: MatchesListViewHolder, position: Int) {
        holder.bindView(getItem(position), itemClick)
    }
}

enum class MatchesSelectType {
    USER, MATCH
}