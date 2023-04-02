package com.bestDate.view.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.bestDate.data.model.Meta
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.ViewSearchBinding
import com.bestDate.presentation.main.search.SearchAdapter

class SearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSearchBinding =
        ViewSearchBinding.inflate(LayoutInflater.from(context), this)

    private val adapter: SearchAdapter = SearchAdapter()

    var openUser: ((ShortUserData?) -> Unit)? = null
    var getUsersByFilter: (() -> Unit)? = null
    var getUsersByFilterInitial: (() -> Unit)? = null

    init {
        setUpSwipe()
        setUpUsersList()
    }

    fun setUsers(users: MutableList<ShortUserData>?) {
        adapter.submitList(users) {
            adapter.loadingMode = false
            binding.loadingBox.isVisible = false
            binding.swipeRefresh.isRefreshing = false
            binding.noDataViewWithLoading.noData = users?.isEmpty() == true
        }
    }

    fun setMeta(meta: Meta?) {
        adapter.meta = meta
    }

    fun setLoading(loading: Boolean) {
        if (!binding.swipeRefresh.isRefreshing && !binding.loadingBox.isVisible) {
            binding.noDataViewWithLoading.noData = false
            binding.noDataViewWithLoading.toggleLoading(loading)
        }
    }

    fun closeLoading() {
        binding.loadingBox.isVisible = false
        binding.noDataViewWithLoading.toggleLoading(false)
        binding.swipeRefresh.isRefreshing = false
    }

    private fun setUpSwipe() {
        binding.swipeRefresh.setOnRefreshListener {
            getUsersByFilterInitial?.invoke()
        }
    }

    private fun setUpUsersList() {
        binding.recyclerViewSearches.layoutManager = GridLayoutManager(context, 2)
        adapter.itemClick = {
            openUser?.invoke(it)
        }
        adapter.loadMoreItems = {
            binding.loadingBox.isVisible = true
            getUsersByFilter?.invoke()
        }
        binding.recyclerViewSearches.adapter = adapter
        binding.noDataViewWithLoading.toggleLoading(true)
    }
}