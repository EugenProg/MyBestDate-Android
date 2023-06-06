package com.bestDate.presentation.main.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.R
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.Meta
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ItemSearchProfilesElementBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bumptech.glide.Glide

class SearchAdapter :
    ListAdapter<ShortUserData, SearchAdapter.SearchProfileViewHolder>(SearchDiffUtils()) {

    var itemClick: ((ShortUserData?) -> Unit)? = null
    var loadMoreItems: (() -> Unit)? = null
    var meta: Meta? = Meta()
    var loadingMode: Boolean = false

    class SearchDiffUtils : DiffUtil.ItemCallback<ShortUserData>() {
        override fun areItemsTheSame(oldItem: ShortUserData, newItem: ShortUserData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShortUserData, newItem: ShortUserData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProfileViewHolder {
        return SearchProfileViewHolder(
            ItemSearchProfilesElementBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchProfileViewHolder, position: Int) {
        holder.bind(getItem(position), itemClick)

        if (position >= itemCount - 1 && meta?.current_page.orZero < meta?.last_page.orZero && !loadingMode) {
            loadingMode = true
            loadMoreItems?.invoke()
        }
    }

    class SearchProfileViewHolder(
        override val binding: ItemSearchProfilesElementBinding
    ) : BaseClickViewHolder<ShortUserData?, ((ShortUserData?) -> Unit)?, ItemSearchProfilesElementBinding>(
        binding
    ) {
        @SuppressLint("SetTextI18n")
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
                    .load(item?.getMainPhoto()?.thumb_url)
                    .centerCrop()
                    .into(binding.profileImageView)
                onlineView.isVisible = item?.is_online == true
                verifyView.isVerified = item?.full_questionnaire

                root.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
            }
        }
    }
}