package com.runidev.qrcode2025.enumData

import android.annotation.SuppressLint

import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.language.Language

object DataConstants {

    @SuppressLint("ConstantLocale")
    val listAppLanguage = mutableListOf(
        Language(name = R.string.language_english, image = R.drawable.ic_flag_uk, "en"),
        Language(name = R.string.language_spanish, image = R.drawable.ic_flag_spanish, "es"),
        Language(name = R.string.language_vietnam, image = R.drawable.ic_flag_vietnam, "vi"),
        Language(name = R.string.language_hindi, image = R.drawable.ic_flag_india, "hi"),
        Language(name = R.string.language_japanese, image = R.drawable.ic_flag_japanese, "ja"),
        Language(name = R.string.language_korean, image = R.drawable.ic_flag_korean, "ko"),
        Language(name = R.string.language_arabic, image = R.drawable.ic_flag_arabic, "ar"),
        Language(name = R.string.language_chinese, image = R.drawable.ic_flag_chinese, "zh"),
        Language(name = R.string.language_dutch, image = R.drawable.ic_flag_dutch, "nl"),
        Language(name = R.string.language_german, image = R.drawable.ic_flag_german, "de"),
        Language(name = R.string.language_ukrainian, image = R.drawable.ic_flag_ukrainian, "uk"),
        Language(name = R.string.language_russian, image = R.drawable.ic_flag_russian, "ru"),
        Language(name = R.string.language_french, image = R.drawable.ic_flag_french, "fr"),
        Language(name = R.string.language_italian, image = R.drawable.ic_flag_italian, "it"),
        Language(name = R.string.language_polish, image = R.drawable.ic_flag_polish, "pl"),
        Language(name = R.string.language_portuguese, image = R.drawable.ic_flag_portuguese, "pt")
    )
}
