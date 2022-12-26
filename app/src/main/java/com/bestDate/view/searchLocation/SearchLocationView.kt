package com.bestDate.view.searchLocation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.hideKeyboard
import com.bestDate.data.extension.setAttrs
import com.bestDate.data.extension.textInputAsFlow
import com.bestDate.data.utils.CityListItem
import com.bestDate.data.utils.LocationAutocompleteUtil
import com.bestDate.databinding.ViewSearchLocationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class SearchLocationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSearchLocationBinding =
        ViewSearchLocationBinding.inflate(LayoutInflater.from(context), this)
    private val autoComplete = LocationAutocompleteUtil(context)
    private lateinit var adapter: SearchResultsAdapter

    var selectAction: ((CityListItem) -> Unit)? = null

    init {
        setAttrs(attrs, R.styleable.SearchLocationView) {
            val style = getStyleById(it.getInt(R.styleable.SearchLocationView_viewSearchStyle, 1))
            setStyle(style)
            adapter = SearchResultsAdapter(style)
        }

        adapter.itemClick = {
            binding.input.setText(it.getLocation())
            selectAction?.invoke(it)
            this.hideKeyboard()
        }

        binding.locationListView.layoutManager = LinearLayoutManager(context)
        binding.locationListView.adapter = adapter
    }

    fun initSearching(
        scope: CoroutineScope,
        lifecycleOwner: LifecycleOwner,
        currentLocation: String?
    ) {
        binding.input.setText(currentLocation)

        binding.input.textInputAsFlow()
            .map { it?.trim() }
            .debounce(350)
            .distinctUntilChanged()
            .onEach { autoComplete.search(it.toString()) }
            .launchIn(scope)

        autoComplete.locationList.observe(lifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setStyle(style: SearchStyle) {
        with(binding) {
            inputBox.setCardBackgroundColor(ContextCompat.getColor(context, style.inputBack))
            hint.setTextColor(ContextCompat.getColor(context, style.hintColor))
            input.setTextColor(ContextCompat.getColor(context, style.inputColor))
        }
    }

    private fun getStyleById(id: Int): SearchStyle {
        return if (id == 0) SearchStyle.LIGHT
        else SearchStyle.DARK
    }

    enum class SearchStyle(
        var inputBack: Int,
        var itemColor: Int,
        var inputColor: Int,
        var hintColor: Int
    ) {
        LIGHT(R.color.light_search_box, R.color.main_90, R.color.main_90, R.color.main_60),
        DARK(R.color.dark_search_box, R.color.white_60, R.color.white_90, R.color.white_60)
    }
}