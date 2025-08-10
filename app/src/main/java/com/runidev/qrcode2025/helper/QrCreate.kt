package com.runidev.qrcode2025.helper

import android.support.annotation.DrawableRes
import com.runidev.qrcode2025.R

enum class QrCreate (val label: String, @DrawableRes val iconRes: Int){
    CLIPBOARD("Clipboard", R.drawable.clipboard),
    WEBSITE("Website", R.drawable.url),
    WIFI("Wi-Fi", R.drawable.connectwifi),

    FACEBOOK("Facebook", R.drawable.faceb),
    YOUTUBE("Youtube", R.drawable.ytb),

    TEXT("Text", R.drawable.text_qr),
    PHONE("Phone", R.drawable.baseline_phone_24),

    EMAIL("E-mail", R.drawable.sendemail),
    SMS("SMS", R.drawable.sendsms),

    PAYPAL("Paypal", R.drawable.paypal),
    INSTAGRAM("Instagram", R.drawable.instagram),

    TWITTER("Twitter", R.drawable.x),
    SPOTIFY("Spotify", R.drawable.spotify),
    VIBER("Viber",R.drawable.viber)

}