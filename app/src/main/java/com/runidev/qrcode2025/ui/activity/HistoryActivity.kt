package com.runidev.qrcode2025.ui.activity

import android.graphics.Color
import android.os.Bundle
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.databinding.ActivityHistoryBinding
import com.runidev.qrcode2025.helper.lightStatusBar

class HistoryActivity : BaseActivity<ActivityHistoryBinding>(ActivityHistoryBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = Color.WHITE
    }
}