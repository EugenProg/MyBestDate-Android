package com.bestDate.view.anotherProfile

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ProfileImage
import com.bestDate.data.model.ShortUserData
import com.bestDate.data.utils.Logger
import com.bestDate.data.utils.ZodiacSign
import com.bestDate.data.utils.ZodiacUtils
import com.bestDate.databinding.ViewAnotherProfileHeaderBinding
import com.bestDate.db.entity.UserDB
import com.bumptech.glide.Glide
import kotlin.math.roundToInt

class AnotherProfileHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAnotherProfileHeaderBinding =
        ViewAnotherProfileHeaderBinding.inflate(LayoutInflater.from(context), this)

    var clickBack: (() -> Unit)? = null
    var clickAdditionally: (() -> Unit)? = null
    var clickAvatar: (() -> Unit)? = null

    init {
        binding.back.onClick = {
            clickBack?.invoke()
        }
        binding.additionalAction.setOnSaveClickListener {
            clickAdditionally?.invoke()
        }
        binding.avatar.setOnSaveClickListener {
            clickAvatar?.invoke()
        }
    }

    fun getTopBoxView(): View = binding.topBox

    fun setUserInfo(user: ShortUserData?) {
        Glide.with(context).load(user?.getMainPhoto()?.full_url).into(binding.avatar)
        binding.online.isVisible = user?.is_online == true
        binding.distance.text =
            context.getString(R.string.distance_unit, user?.distance?.roundToInt().orZero)
        binding.zodiac.text = context.getString(ZodiacUtils().getZodiacSignByDate(user?.birthday).title)
        isBlocked(user?.blocked_me)
    }

    fun setUserInfo(user: UserDB?) {
        Glide.with(context).load(user?.getMainPhoto()?.full_url).into(binding.avatar)
        binding.online.isVisible = user?.is_online == true
        binding.distance.text =
            context.getString(R.string.distance_unit, user?.distance?.roundToInt().orZero)
        binding.zodiac.text = context.getString(ZodiacUtils().getZodiacSignByDate(user?.birthday).title)
        isBlocked(user?.blocked_me)
    }

    fun isBlocked(blocked: Boolean?) {
        val alpha = if (blocked == true) 0.3F else 1F
        with(binding) {
            online.alpha = alpha
            distance.alpha = alpha
            zodiac.alpha = alpha
        }
    }
}