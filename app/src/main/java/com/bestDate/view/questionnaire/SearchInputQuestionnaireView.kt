package com.bestDate.view.questionnaire

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleCoroutineScope
import com.bestDate.R
import com.bestDate.data.extension.textInputAsFlow
import com.bestDate.data.extension.textIsChanged
import com.bestDate.databinding.ViewSearchInputQuestionnaireBinding
import kotlinx.coroutines.flow.*

class SearchInputQuestionnaireView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSearchInputQuestionnaireBinding

    var textIsChanged: ((String) -> Unit)? = null
    var textIsChangedFlow: ((String) -> Unit)? = null

    init {
        val view = View.inflate(context, R.layout.view_search_input_questionnaire, this)
        binding = ViewSearchInputQuestionnaireBinding.bind(view)

        binding.searchInput.textIsChanged { textIsChanged?.invoke(it) }
    }

    fun flowTextChangeInvoker(scope: LifecycleCoroutineScope) {
        binding.searchInput.textInputAsFlow()
            .map { it?.trim() }
            .debounce(350)
            .distinctUntilChanged()
            .onEach { textIsChangedFlow?.invoke(it.toString()) }
            .launchIn(scope)
    }

    var hint: String
        get() = binding.placeholder.text.toString()
        set(value) {
            binding.placeholder.text = value
        }

    var text: String
        get() = binding.searchInput.text.toString()
        set(value) {
            binding.searchInput.setText(value)
        }


    fun setFocus() {
        binding.searchInput.isFocusableInTouchMode = true
        binding.searchInput.requestFocus()
    }
}