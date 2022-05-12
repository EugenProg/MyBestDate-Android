package com.bestDate.view.bottomSheet.GenderSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.databinding.GenderSheetDialogBinding
import com.bestDate.view.base.BaseBottomSheetDialog
import com.hadilq.liveevent.LiveEvent

class GenderSheetDialog: BaseBottomSheetDialog<GenderSheetDialogBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> GenderSheetDialogBinding =
        { inflater, parent, attach -> GenderSheetDialogBinding.inflate(inflater, parent, attach) }

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
        binding.genderList.adapter = adapter
        binding.genderList.layoutManager = LinearLayoutManager(requireContext())
    }
}