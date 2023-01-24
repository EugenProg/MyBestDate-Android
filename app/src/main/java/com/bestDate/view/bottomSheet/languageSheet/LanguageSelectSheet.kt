package com.bestDate.view.bottomSheet.languageSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.databinding.SheetItemListBinding
import com.bestDate.view.base.BaseBottomSheet

class LanguageSelectSheet : BaseBottomSheet<SheetItemListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetItemListBinding =
        { inflater, parent, attach -> SheetItemListBinding.inflate(inflater, parent, attach) }

    private lateinit var adapter: LanguageSelectAdapter
    var itemClick: ((LanguageType) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        val languageList: MutableList<LanguageType> = mutableListOf()
        LanguageType.values().forEach {
            languageList.add(it)
        }
        binding.title.text = getString(R.string.language)

        adapter = LanguageSelectAdapter(languageList)
        binding.itemList.adapter = adapter
        binding.itemList.layoutManager = LinearLayoutManager(requireContext())

        adapter.itemClick = {
            dismiss()
            if (it.settingsName != getString(R.string.app_locale)) itemClick?.invoke(it)
        }
    }
}