package com.runidev.qrcode2025.helper

import com.f2prateek.rx.preferences2.RxSharedPreferences
import javax.inject.Inject

class Preferences  @Inject constructor(private val rxPref: RxSharedPreferences) {
    val isUpgraded = rxPref.getBoolean("isUpgraded", false)
    val isBeep = rxPref.getBoolean("isBeep", false)
    val isAutoCopy = rxPref.getBoolean("isAutoCopy", false)
    val isVibrate = rxPref.getBoolean("isVibrate", true)

    val positionSearchEngine = rxPref.getInteger("positionSearchEngine", 0)
    val dataSearch = rxPref.getString("DATA_SEARCH", "Google")


    val keyAppLanguage = rxPref.getString("keyAppLanguage", "en")
    val isConfigLanguage = rxPref.getBoolean("isConfigLanguage", false)

}
