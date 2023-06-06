package com.bestDate.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.model.DuelProfile
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewResultBinding

class ResultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: ViewResultBinding =
        ViewResultBinding.inflate(LayoutInflater.from(context), this)

    var openProfile: ((ShortUserData?) -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.ResultView) {
            binding.headerTextview.root.text = it.getString(R.styleable.ResultView_result_title)
        }

        binding.firstResultProfileView.clickAction = {
            openProfile?.invoke(it)
        }
        binding.secondResultProfileView.clickAction = {
            openProfile?.invoke(it)
        }
    }

    var duelProfiles: MutableList<DuelProfile>? = mutableListOf()
        set(value) {
            binding.firstResultProfileView.profile = value?.firstOrNull()
            binding.secondResultProfileView.profile = value?.lastOrNull()
            field = value
        }
}