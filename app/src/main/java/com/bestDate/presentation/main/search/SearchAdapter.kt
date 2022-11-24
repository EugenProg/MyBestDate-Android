package com.bestDate.presentation.main.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.databinding.ItemSearchProfilesElementBinding

class SearchAdapter : ListAdapter<ProfileData, RecyclerView.ViewHolder>(SearchDiffUtils()) {

    class SearchDiffUtils : DiffUtil.ItemCallback<ProfileData>() {
        override fun areItemsTheSame(oldItem: ProfileData, newItem: ProfileData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfileData, newItem: ProfileData): Boolean {
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
        fun bind(item: ProfileData) {
            binding.run {
                nameTextVIew.text = item.name
                locationTextView.text = item.city
                ageTextView.text = item.age
                distanceTextView.text = item.distance
            }
        }
    }
}

data class ProfileData(
    val id: Int,
    val name: String,
    val city: String,
    val age: String,
    val distance: String
)