package com.bestDate.view.match

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewMatchBinding

class MatchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewMatchBinding =
        ViewMatchBinding.inflate(LayoutInflater.from(context), this)

    private val matchesList: MutableList<ShortUserData?> = mutableListOf()

    var openQuestionnaireClick: ((userId: Int?) -> Unit)? = null
    var matchAction: ((userId: Int?) -> Unit)? = null
    var nextUser: ((lastUser: Boolean) -> Unit)? = null
    var clickAction: ((ShortUserData?) -> Unit)? = null

    init {
        binding.questionnaireButton.click = {
            if (matchesList.isNotEmpty())
                openQuestionnaireClick?.invoke(matchesList.lastOrNull()?.id)
        }
    }

    private fun getView(user: ShortUserData?): View {
        val view = MatchImageView(context)
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        view.setUser(user)

        view.matchAction = { matchView, userId ->
            matchAction?.invoke(userId)
            removeMatchById(matchView)
        }
        view.clickAction = {
            clickAction?.invoke(it)
        }
        view.closeAction = {
            removeMatchById(it)
        }
        return view
    }

    private fun removeMatchById(matchView: View?) {
        if (matchesList.isNotEmpty()) {
            matchesList.removeLast()
            binding.matchesBox.removeView(matchView)
            nextUser?.invoke(matchesList.isEmpty())
            setCurrentUserInfo()
        }
    }

    private fun setCurrentUserInfo() {
        matchesList.lastOrNull()?.let {
            with(binding) {
                nameView.text = it.name
                age.text = it.getAge()
                location.text = it.getLocation()
                onlineView.isVisible = it.is_online == true
                distanceView.text = it.distance?.let {
                    context.getString(R.string.distance_km, it.toInt())
                } ?: ""
                nameView.requestLayout()
            }
        }
    }

    fun setMatches(it: MutableList<ShortUserData>?) {
        matchesList.clear()
        it?.forEach {
            matchesList.add(it)
            binding.matchesBox.addView(getView(it))
        }
        setCurrentUserInfo()
    }
}