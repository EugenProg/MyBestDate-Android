package com.bestDate.presentation.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.R
import com.bestDate.base.BaseClickViewHolder
import com.bestDate.base.BaseViewHolder
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemLoadingSearchElementBinding
import com.bestDate.databinding.ItemSearchProfilesElementBinding
import com.bumptech.glide.Glide
import kotlin.Boolean
import kotlin.Int
import kotlin.Unit
import kotlin.run

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
                    ItemLoadingSearchElementBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            else -> {
                LoadingProfileViewHolder(
                    ItemLoadingSearchElementBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchProfileViewHolder) holder.bind(getItem(position), itemClick)

        if (position >= itemCount - perPage / 2) {
            loadMoreItems?.invoke()
        }
    }

    class SearchProfileViewHolder(
        override val binding: ItemSearchProfilesElementBinding
    ) : BaseClickViewHolder<ShortUserData?, ((ShortUserData?) -> Unit)?, ItemSearchProfilesElementBinding>(
        binding
    ) {
        override fun bindView(item: ShortUserData?, itemClick: ((ShortUserData?) -> Unit)?) {
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
                    .placeholder(R.drawable.ic_default_photo)
                    .into(binding.profileImageView)
                onlineView.isVisible = item?.is_online == true

                root.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
            }
        }
    }

    class LoadingProfileViewHolder(override val binding: ItemLoadingSearchElementBinding) :
        BaseViewHolder<ShortUserData, ItemLoadingSearchElementBinding>(binding) {
    }

    override fun getItemViewType(position: Int): Int {
        return if (position in itemCount - perPage / 5..itemCount && itemCount != total) LOADING else ITEM
    }

    override fun getItem(position: Int): ShortUserData? {
        return currentList[position]
    }

}