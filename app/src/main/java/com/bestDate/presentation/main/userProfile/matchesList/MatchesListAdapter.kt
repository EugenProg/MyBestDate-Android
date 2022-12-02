package com.bestDate.presentation.main.userProfile.matchesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.base.BaseClickViewHolder
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.Like
import com.bestDate.data.model.Match
import com.bestDate.databinding.ItemMatchesListBinding
import com.bumptech.glide.Glide

class MatchesListAdapter(private val myPhotoUrl: String) :
    ListAdapter<Match, MatchesListAdapter.MatchesListViewHolder>(MatchesDiffUtils()) {

    var itemClick: ((Match, MatchesSelectType) -> Unit)? = null

    class MatchesDiffUtils : DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem == newItem
        }
    }

    class MatchesListViewHolder(override val binding: ItemMatchesListBinding, private val myPhotoUrl: String) :
        BaseClickViewHolder<Match, ((Match, MatchesSelectType) -> Unit)?, ItemMatchesListBinding>(binding) {
        override fun bindView(item: Match, itemClick: ((Match, MatchesSelectType) -> Unit)?) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.user?.main_photo?.thumb_url)
                    .circleCrop()
                    .into(avatar)
                Glide.with(itemView.context)
                    .load(myPhotoUrl)
                    .into(myPhoto)
                Glide.with(itemView.context)
                    .load(item.user?.main_photo?.thumb_url)
                    .into(userPhoto)
                name.text = item.user?.name
                location.text = item.user?.getLocation()
                time.text = item.getTime()

                root.setOnSaveClickListener {
                    itemClick?.invoke(item, MatchesSelectType.USER)
                }

                matchesBox.setOnSaveClickListener {
                    itemClick?.invoke(item, MatchesSelectType.MATCH)
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