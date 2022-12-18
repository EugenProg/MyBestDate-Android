package com.bestDate.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.R
import com.bestDate.data.extension.setAttrs
import com.bestDate.databinding.ViewTitleDescriptionSwitchBinding

class TitleDescriptionSwitchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewTitleDescriptionSwitchBinding =
        ViewTitleDescriptionSwitchBinding.inflate(LayoutInflater.from(context), this)
    var checkAction: ((checked: Boolean) -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.TitleDescriptionSwitchView) {
            with(binding) {
                titleDescription.title =
                    it.getString(R.styleable.TitleDescriptionSwitchView_t_d_s_title)
                titleDescription.description =
                    it.getString(R.styleable.TitleDescriptionSwitchView_t_d_s_description)
                switchView.title =
                    it.getString(R.styleable.TitleDescriptionSwitchView_t_d_s_switch_title)
                switchView.icon = it.getResourceId(
                    R.styleable.TitleDescriptionSwitchView_t_d_s_switch_icon,
                    R.drawable.ic_settings_message
                )
            }
        }

        binding.switchView.checkAction = {
            checkAction?.invoke(it)
        }
    }

    fun setChecked(isChecked: Boolean?) {
        binding.switchView.setChecked(isChecked)
    }
}