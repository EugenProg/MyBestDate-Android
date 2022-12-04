package com.bestDate.view.anotherProfile

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewAnotherProfileBlockedBinding
import com.bestDate.db.entity.UserDB

class AnotherProfileBlockedView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAnotherProfileBlockedBinding =
        ViewAnotherProfileBlockedBinding.inflate(LayoutInflater.from(context), this)

    fun setUserInfo(user: ShortUserData?) {
        val isBlocked = user?.blocked_me == true
        binding.root.isVisible = isBlocked
        if (isBlocked) {
            with(binding.userInfoView) {
                root.alpha = 0.3f
                name.text = user?.name
                age.text = user?.getAge()
                location.text = user?.getLocation()
            }
        }
    }

    fun setUserInfo(user: UserDB?) {
        val isBlocked = user?.blocked_me == true
        binding.root.isVisible = isBlocked
        if (isBlocked) {
            with(binding.userInfoView) {
                root.alpha = 0.3f
                name.text = user?.name
                age.text = user?.getAge()
                location.text = user?.getUserLocation()
            }
        }
    }
}