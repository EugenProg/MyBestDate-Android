package com.bestDate.view.bottomSheet.optionsSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.databinding.SheetImageListBinding
import com.bestDate.view.base.BaseBottomSheet

class OptionsSheet(
    private var optionsList: HashMap<String, String>,
    var title: String
) : BaseBottomSheet<SheetImageListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetImageListBinding =
        { inflater, parent, attach -> SheetImageListBinding.inflate(inflater, parent, attach) }

    private lateinit var adapter: StringListAdapter
    var itemClick: ((String) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        adapter = StringListAdapter(optionsList.keys.toMutableList()) {
            dismiss()
            itemClick?.invoke(it)
        }

        binding.title.text = title

        binding.itemList.adapter = adapter
        binding.itemList.layoutManager = LinearLayoutManager(requireContext())
    }
}