package com.bestDate.view.bottomSheet.languageSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemBottomSheetListBinding
import com.bestDate.presentation.base.BaseClickViewHolder

class LanguageSelectAdapter(private val languages: MutableList<LanguageType>) :
    RecyclerView.Adapter<LanguageSelectAdapter.LanguageSelectViewHolder>() {

    var itemClick: ((LanguageType) -> Unit)? = null

    class LanguageSelectViewHolder(override val binding: ItemBottomSheetListBinding) :
        BaseClickViewHolder<LanguageType, ((LanguageType) -> Unit)?, ItemBottomSheetListBinding>(
            binding
        ) {
        override fun bindView(item: LanguageType, itemClick: ((LanguageType) -> Unit)?) {
            binding.name.text = item.language

            itemView.setOnSaveClickListener {
                itemClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageSelectViewHolder {
        return LanguageSelectViewHolder(
            ItemBottomSheetListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: LanguageSelectViewHolder, position: Int) {
        holder.bind(languages[position], itemClick)
    }

    override fun getItemCount(): Int = languages.size
}

enum class LanguageType(var language: String, var settingsName: String) {
    ENGLISH("English", "en"),
    GERMAN("Deutsch", "de"),
    UKRAINIAN("Українська", "uk"),
    RUSSIA("Русский", "ru")
}