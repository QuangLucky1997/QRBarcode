package com.runidev.qrcode2025.language

import android.os.Bundle
import com.runidev.qrcode2025.util.clickWithAnimationDebounce
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.databinding.ActivityLanguageBinding
import com.runidev.qrcode2025.helper.Preferences
import com.runidev.qrcode2025.helper.lightNavigationBar
import com.runidev.qrcode2025.util.startMain
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageBinding>(ActivityLanguageBinding::inflate) {

    @Inject
    lateinit var prefs: Preferences

    @Inject
    lateinit var languageAdapter: LanguageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            navigationBarColor = resources.getColor(R.color.white, null)
            statusBarColor = resources.getColor(R.color.colorMain, null)
        }
//        initUISystem()
        lightNavigationBar()
        initData()
        listenerView()
    }

    private fun initData() {
        binding.recyclerLanguage.adapter = languageAdapter
    }

    private fun listenerView() {
        languageAdapter.itemClick = {
            languageAdapter.itemLanguage = it
        }

        binding.apply {
            viewDone.clickWithAnimationDebounce {
                prefs.keyAppLanguage.set(languageAdapter.itemLanguage.key)
                startMain { finish() }
                prefs.isConfigLanguage.set(true)
            }
        }
    }
}