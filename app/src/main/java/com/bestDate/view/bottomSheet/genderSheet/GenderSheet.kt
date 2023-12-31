package com.bestDate.view.bottomSheet.genderSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.databinding.SheetItemListBinding
import com.bestDate.presentation.registration.GenderType
import com.bestDate.view.base.BaseBottomSheet

class GenderSheet: BaseBottomSheet<SheetItemListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetItemListBinding =
        { inflater, parent, attach -> SheetItemListBinding.inflate(inflater, parent, attach) }

    private lateinit var adapter: GenderSheetAdapter
    private var genderList: MutableList<GenderType> = ArrayList()
    var itemClick: ((GenderType) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        genderList.clear()
        GenderType.values().forEach {
            genderList.add(it)
        }

        adapter = GenderSheetAdapter(genderList) {
            dismiss()
            itemClick?.invoke(it)
        }

        binding.title.text = getString(R.string.gender)

        binding.itemList.adapter = adapter
        binding.itemList.layoutManager = LinearLayoutManager(requireContext())
    }
}