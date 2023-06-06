package com.bestDate.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.DuelProfile
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewResultProfileBinding
import com.bumptech.glide.Glide

class ResultProfileView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewResultProfileBinding =
        ViewResultProfileBinding.inflate(LayoutInflater.from(context), this)

    var clickAction: ((ShortUserData?) -> Unit)? = null

    init {
        binding.root.setOnSaveClickListener {
            clickAction?.invoke(profile?.user)
        }
    }

    var profile: DuelProfile? = DuelProfile()
        @SuppressLint("SetTextI18n")
        set(value) {
            with(binding)
            {
                val user = value?.user
                nameTextView.text = user?.name
                locationTextView.text = "${user?.location?.city}, ${user?.location?.country}"
                ageTextView.text = user?.getAge()
                Glide.with(binding.root.context)
                    .load(value?.thumb_url)
                    .centerCrop()
                    .into(binding.profileImageView)
                verifyView.isVerified = user?.full_questionnaire
                barView.percent = value?.rating
            }
            field = value
        }
}