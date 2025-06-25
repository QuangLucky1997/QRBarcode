package com.runidev.qrcode2025.util.ext

import android.os.SystemClock
import android.view.View


fun View.clicks(debounce: Long = 250, withAnim: Boolean = true, scale: Float = 0.96F, clicks: (View) -> Unit) {
    if (withAnim) {
        var lastClickTime: Long = 0
        PushDownAnim.setPushDownAnimTo(this)
            .setScale(PushDownAnim.MODE_SCALE, scale)
            .setOnClickListener {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounce) return@setOnClickListener
                else clicks(this)
                lastClickTime = SystemClock.elapsedRealtime()
            }
    } else {
        var lastClickTime: Long = 0
        setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounce) return@setOnClickListener
            else clicks(this)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    }
}