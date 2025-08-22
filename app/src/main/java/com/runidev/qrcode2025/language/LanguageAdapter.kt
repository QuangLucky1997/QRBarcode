package com.runidev.qrcode2025.language

import android.view.LayoutInflater
import android.view.ViewGroup
import com.runidev.qrcode2025.enumData.DataConstants
import com.runidev.qrcode2025.util.clickWithAnimationDebounce
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseAdapter
import com.runidev.qrcode2025.databinding.LayoutItemLanguageBinding
import com.runidev.qrcode2025.helper.Preferences
import javax.inject.Inject

class LanguageAdapter @Inject constructor(
    private val preferences: Preferences
) : BaseAdapter<Language, LayoutItemLanguageBinding>() {

    var itemLanguage =
        DataConstants.listAppLanguage.find { language -> language.key == preferences.keyAppLanguage.get() }
            ?: DataConstants.listAppLanguage[0]
        set(value) {
            if (field == value) return

            data.indexOf(value).takeIf { it != -1 }?.let { notifyItemChanged(it) }
            data.indexOf(field).takeIf { it != -1 }?.let { notifyItemChanged(it) }

            field = value
        }
    var itemClick: ((Language) -> Unit)? = null

    init {
        data = DataConstants.listAppLanguage
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> LayoutItemLanguageBinding
        get() = LayoutItemLanguageBinding::inflate

    override fun bindItem(item: Language, binding: LayoutItemLanguageBinding, position: Int) {
        binding.apply {
            imageLanguage.setImageResource(item.image)
            textLanguage.text = root.context.resources.getString(item.name)

            layoutLang.setBackgroundResource(if (itemLanguage == item) R.drawable.bg_gradient_main else R.drawable.bg_language)
            checkBox.setImageResource(if (itemLanguage == item) R.drawable.ic_checked_language else R.drawable.ic_uncheck_language)

            selectLanguage.clickWithAnimationDebounce {
                itemClick?.invoke(item)
            }

            checkBox.clickWithAnimationDebounce {
                itemClick?.invoke(item)
            }
        }
    }
}