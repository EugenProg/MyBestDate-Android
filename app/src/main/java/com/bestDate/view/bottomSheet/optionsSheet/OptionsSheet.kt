package com.bestDate.view.bottomSheet.optionsSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.databinding.SheetItemListBinding
import com.bestDate.presentation.main.search.FilterType
import com.bestDate.view.base.BaseBottomSheet

class OptionsSheet(
    private var optionsList: LinkedHashMap<FilterType, String>,
    private var title: String
) : BaseBottomSheet<SheetItemListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetItemListBinding =
        { inflater, parent, attach -> SheetItemListBinding.inflate(inflater, parent, attach) }

    private lateinit var adapter: FilterListAdapter
    var itemClick: ((Pair<FilterType, String>) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        val list: MutableList<Pair<FilterType, String>> = mutableListOf()
        optionsList.forEach { (filterType, name) ->
            list.add(Pair(filterType, name))
        }
        adapter = FilterListAdapter(list) {
            dismiss()
            itemClick?.invoke(it)
        }

        binding.title.text = title

        binding.itemList.adapter = adapter
        binding.itemList.layoutManager = LinearLayoutManager(requireContext())
    }
}