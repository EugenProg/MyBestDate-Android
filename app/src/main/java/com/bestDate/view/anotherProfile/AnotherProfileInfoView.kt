package com.bestDate.view.anotherProfile

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.data.extension.hideWithAlphaAnimation
import com.bestDate.data.model.ProfileImage
import com.bestDate.data.model.ShortUserData
import com.bestDate.data.utils.Logger
import com.bestDate.databinding.ViewAnotherProfileInfoBinding
import com.bestDate.db.entity.UserDB
import com.bestDate.presentation.main.userProfile.ImageLineAdapter

class AnotherProfileInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAnotherProfileInfoBinding =
        ViewAnotherProfileInfoBinding.inflate(LayoutInflater.from(context), this)

    var openQuestionnaire: (() -> Unit)? = null
    var imageClick: ((ProfileImage?) -> Unit)? = null
    private var adapter: ImageLineAdapter

    init {
        binding.questionnaireView.click = {
            openQuestionnaire?.invoke()
        }

        val height = resources.displayMetrics.widthPixels / 3
        adapter = ImageLineAdapter(height, false)
        with(binding) {
            imageListView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            imageListView.adapter = adapter
        }
        adapter.imageClick = {
            imageClick?.invoke(it)
        }
    }

    fun setUserInfo(user: ShortUserData?) {
        val isBlocked = user?.blocked_me != false
        binding.root.isVisible = !isBlocked
        if (!isBlocked) {
            with(binding.userInfoView) {
                name.text = user?.name
                age.text = user?.getAge()
                location.text = user?.getLocation()
                verifyView.isVerified = user?.full_questionnaire
                binding.imageLinePreview.setImagesCount(user?.photos_count ?: 3)
            }
        }
    }

    fun setUserInfo(user: UserDB?) {
        val isBlocked = user?.blocked_me != false
        binding.root.isVisible = !isBlocked
        if (!isBlocked) {
            binding.imageListView.isVisible = true
            binding.imageLinePreview.hideWithAlphaAnimation()
            with(binding.userInfoView) {
                adapter.submitList(user?.photos)
                name.text = user?.name
                age.text = user?.getAge()
                location.text = user?.getUserLocation()
                verifyView.isVerified = user?.questionnaireFull()
            }
        }
    }
}