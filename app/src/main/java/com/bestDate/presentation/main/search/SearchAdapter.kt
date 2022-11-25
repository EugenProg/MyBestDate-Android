package com.bestDate.presentation.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.R
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemSearchProfilesElementBinding
import com.bumptech.glide.Glide

class SearchAdapter : ListAdapter<ShortUserData, RecyclerView.ViewHolder>(SearchDiffUtils()) {

    class SearchDiffUtils : DiffUtil.ItemCallback<ShortUserData>() {
        override fun areItemsTheSame(oldItem: ShortUserData, newItem: ShortUserData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShortUserData, newItem: ShortUserData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchProfileViewHolder(
            ItemSearchProfilesElementBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SearchProfileViewHolder).bind(getItem(position))
    }

    inner class SearchProfileViewHolder(val binding: ItemSearchProfilesElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShortUserData) {
            binding.run {
                nameTextView.text = item.name
                locationTextView.text = "${item.location?.city}, ${item.location?.country}"
                ageTextView.text = item.getAge()
                distanceTextView.text = binding.root.context.getString(R.string.distance_km, (item.distance ?: 0.0).toInt())
                Glide.with(binding.root.context)
                    .load(item.main_photo?.thumb_url)
                    .into(binding.profileImageView)
            }
        }
    }
}