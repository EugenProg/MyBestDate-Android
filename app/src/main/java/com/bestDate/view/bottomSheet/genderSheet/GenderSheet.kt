package com.bestDate.view.bottomSheet.genderSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.databinding.SheetImageListBinding
import com.bestDate.view.base.BaseBottomSheet

class GenderSheet: BaseBottomSheet<SheetImageListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetImageListBinding =
        { inflater, parent, attach -> SheetImageListBinding.inflate(inflater, parent, attach) }

    private lateinit var adapter: GenderSheetAdapter
    private var genderList: MutableList<String> = ArrayList()
    var itemClick: ((String) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        genderList.clear()
        genderList.add("Woman, looking for a man")
        genderList.add("Woman, looking for a woman")
        genderList.add("Man, looking for a man")
        genderList.add("Man, looking for a woman")

        adapter = GenderSheetAdapter(genderList) {
            dismiss()
            itemClick?.invoke(it)
        }

        binding.title.text = getString(R.string.gender)

        binding.itemList.adapter = adapter
        binding.itemList.layoutManager = LinearLayoutManager(requireContext())
    }
}