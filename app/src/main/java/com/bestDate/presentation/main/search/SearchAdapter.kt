package com.bestDate.presentation.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemSearchProfilesElementBinding
import com.bumptech.glide.Glide

const val LOADING = 0
const val ITEM = 1

class SearchAdapter : ListAdapter<ShortUserData, RecyclerView.ViewHolder>(SearchDiffUtils()) {

    var itemClick: ((ShortUserData?) -> Unit)? = null
    var loadMoreItems: (() -> Unit)? = null
    var perPage: Int = 0
    var total: Int = 0

    class SearchDiffUtils : DiffUtil.ItemCallback<ShortUserData>() {
        override fun areItemsTheSame(oldItem: ShortUserData, newItem: ShortUserData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShortUserData, newItem: ShortUserData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM -> SearchProfileViewHolder(
                ItemSearchProfilesElementBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            LOADING ->
                LoadingProfileViewHolder(
                    ItemSearchProfilesElementBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            else -> {
                LoadingProfileViewHolder(
                    ItemSearchProfilesElementBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                (holder as SearchProfileViewHolder).bind(getItem(position), itemClick)
            }
        }

        if (position == itemCount - 3) {
            loadMoreItems?.invoke()
        }
    }

    class SearchProfileViewHolder(val binding: ItemSearchProfilesElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShortUserData?, itemClick: ((ShortUserData?) -> Unit)?) {
            binding.run {
                nameTextView.text = item?.name
                locationTextView.text = "${item?.location?.city}, ${item?.location?.country}"
                ageTextView.text = item?.getAge()
                distanceTextView.text = binding.root.context.getString(
                    R.string.distance_km,
                    (item?.distance ?: 0.0).toInt()
                )
                Glide.with(binding.root.context)
                    .load(item?.main_photo?.thumb_url)
                    .into(binding.profileImageView)

                root.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
            }
        }
    }

    class LoadingProfileViewHolder(val binding: ItemSearchProfilesElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) LOADING else ITEM
    }

    override fun getItem(position: Int): ShortUserData? {
        return currentList[position]
    }

}