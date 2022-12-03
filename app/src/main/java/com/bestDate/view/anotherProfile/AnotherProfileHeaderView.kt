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
import com.bestDate.data.utils.Logger
import com.bestDate.data.utils.ZodiacSign
import com.bestDate.data.utils.ZodiacUtils
import com.bestDate.databinding.ViewAnotherProfileHeaderBinding
import com.bumptech.glide.Glide
import kotlin.math.roundToInt

class AnotherProfileHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAnotherProfileHeaderBinding =
        ViewAnotherProfileHeaderBinding.inflate(LayoutInflater.from(context), this)

    var clickBack: (() -> Unit)? = null
    var clickAdditionally: (() -> Unit)? = null

    init {
        binding.back.onClick = {
            clickBack?.invoke()
        }
        binding.additionalAction.setOnSaveClickListener {
            clickAdditionally?.invoke()
        }
    }

    fun getTopBoxView(): View = binding.topBox

    fun setImage(image: ProfileImage?) {
        Glide.with(context).load(image?.full_url).into(binding.avatar)
    }

    fun isOnline(isOnline: Boolean?) {
        binding.online.isVisible = isOnline == true
    }

    fun setDistance(distance: Double?) {
        binding.distance.text =
            context.getString(R.string.distance_unit, distance?.roundToInt().orZero)
    }

    fun setZodiac(birthday: String?) {
        binding.zodiac.text = context.getString(ZodiacUtils().getZodiacSignByDate(birthday).title)
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