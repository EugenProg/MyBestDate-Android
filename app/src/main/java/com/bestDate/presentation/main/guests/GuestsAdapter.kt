package com.bestDate.presentation.main.guests

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.data.model.Guest
import com.bestDate.data.model.ListItemType
import com.bestDate.data.model.Meta
import com.bestDate.databinding.ItemGuestBinding
import com.bestDate.databinding.ItemHeaderBinding
import com.bestDate.databinding.ItemLoaderBinding
import com.bestDate.presentation.base.GuestBaseViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class GuestsAdapter : ListAdapter<Guest, GuestBaseViewHolder<*>>(GuestsDiffUtils()) {

    var itemClick: ((Guest?) -> Unit)? = null
    var loadMoreItems: (() -> Unit)? = null
    var meta: Meta? = Meta()
    var loadingMode: Boolean = false
    var guestsVisibility: Boolean = true
    var blurPhoto = 8.toPx()
    var blurLine = 4.toPx()

    class GuestsDiffUtils : DiffUtil.ItemCallback<Guest>() {
        override fun areItemsTheSame(oldItem: Guest, newItem: Guest): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Guest, newItem: Guest): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestBaseViewHolder<*> {
        return when (viewType) {
            ListItemType.HEADER.ordinal -> GuestHeaderViewHolder(
                ItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            ListItemType.LOADER.ordinal -> GuestLoadingViewHolder(
                ItemLoaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> GuestViewHolder(
                ItemGuestBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), itemClick, guestsVisibility, blurPhoto, blurLine
            )
        }
    }

    override fun onBindViewHolder(holder: GuestBaseViewHolder<*>, position: Int) {
        holder.bind(getItem(position))

        if (position >= itemCount - 1 && meta?.current_page.orZero < meta?.last_page.orZero && !loadingMode) {
            setLoadingMode()
        }
    }

    class GuestViewHolder(
        override val binding: ItemGuestBinding, override val itemClick: ((Guest) -> Unit)?,
        val visible: Boolean, var photoBlur: Int, var lineBlur: Int
    ) : GuestBaseViewHolder<ItemGuestBinding>(binding, itemClick) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: Guest) {
            binding.run {
                setVisibility()
                newVisitTextView.isVisible = item.viewed == false
                timeTextView.text = item.visit_at.getVisitPeriod(binding.root.context)
                if (visible) {
                    nameTextView.text = item.guest?.name
                    locationTextView.text =
                        "${item.guest?.location?.city}, ${item.guest?.location?.country}"
                    ageTextView.text = item.guest?.getAge()
                    Glide.with(binding.root.context)
                        .load(item.guest?.getMainPhoto()?.thumb_url)
                        .centerCrop()
                        .into(profileImageView)
                    verifyView.isVerified = item.guest?.full_questionnaire
                } else {
                    Glide.with(binding.root.context)
                        .load(item.guest?.getMainPhoto()?.thumb_url)
                        .centerCrop()
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(photoBlur)))
                        .into(profileImageView)

                    val multi = MultiTransformation(
                        BlurTransformation(lineBlur),
                        RoundedCornersTransformation(10, 0)
                    )
                    Glide.with(itemView.context)
                        .load(R.drawable.guests_blur)
                        .apply(RequestOptions.bitmapTransform(multi))
                        .into(smallBlurView)

                    Glide.with(itemView.context)
                        .load(R.drawable.guests_blur)
                        .apply(RequestOptions.bitmapTransform(multi))
                        .into(blurView)
                }

                root.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
            }
        }

        private fun setVisibility() {
            with(binding) {
                locationBlurBox.isVisible = !visible
                nameBlurBox.isVisible = !visible
                nameView.isVisible = visible
                locationTextView.isVisible = visible
                ageTextView.isVisible = visible
                yearTextView.isVisible = visible
            }
        }
    }

    class GuestHeaderViewHolder(override val binding: ItemHeaderBinding) :
        GuestBaseViewHolder<ItemHeaderBinding>(binding, null) {
        override fun bind(item: Guest) {
            with(binding) {
                root.text = item.guest?.id?.let { itemView.context.getString(it) }
            }
        }
    }

    class GuestLoadingViewHolder(override val binding: ItemLoaderBinding) :
        GuestBaseViewHolder<ItemLoaderBinding>(binding, null)

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType?.ordinal ?: ListItemType.HEADER.ordinal
    }

    private fun setLoadingMode() {
        val newList: MutableList<Guest> = mutableListOf()
        newList.addAll(currentList)
        newList.add(loadingItem)
        submitList(newList)
        loadingMode = true
        loadMoreItems?.invoke()
    }

    override fun submitList(list: MutableList<Guest>?, commitCallback: Runnable?) {
        loadingMode = false
        super.submitList(list, commitCallback)
    }

    private var loadingItem: Guest =
        Guest(id = 0, itemType = ListItemType.LOADER)
}