package com.bestDate.view.bottomSheet.genderFilterSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.databinding.SheetItemListBinding
import com.bestDate.presentation.main.search.GenderFilter
import com.bestDate.view.base.BaseBottomSheet

class GenderFilterBottomSheet(private val selectedItem: GenderFilter) :
    BaseBottomSheet<SheetItemListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetItemListBinding =
        { inflater, parent, attach -> SheetItemListBinding.inflate(inflater, parent, attach) }

    private lateinit var adapter: GenderFilterAdapter
    var itemClick: ((GenderFilter) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        adapter = GenderFilterAdapter(GenderFilter.values(), selectedItem)

        adapter.itemClick = {
            dismiss()
            itemClick?.invoke(it)
        }

        binding.title.text = getString(R.string.gender)

        binding.itemList.adapter = adapter
        binding.itemList.layoutManager = LinearLayoutManager(requireContext())
    }
}