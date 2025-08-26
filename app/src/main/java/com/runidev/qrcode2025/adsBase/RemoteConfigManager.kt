package com.runidev.qrcode2025.adsBase

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

object RemoteConfigManager {

    @SuppressLint("StaticFieldLeak")
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    fun init() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 // fetch mỗi 1h, test có thể set = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // 🔹 Defaults: phòng khi chưa fetch được server
        val defaults = mapOf(
            "ads_global_enabled" to true,
            "banner_enabled" to true,
            "native_enabled" to true,
            "interstitial_enabled" to true,
            "interstitial_cooldown" to 60L,
            "rewarded_enabled" to true,
            "open_ads_enabled" to true
        )
        remoteConfig.setDefaultsAsync(defaults)

        // 🔹 Fetch & activate
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("RemoteConfig", "Fetch success")
                } else {
                    Log.e("RemoteConfig", "Fetch failed: ${task.exception?.message}")
                }
            }
    }

    // ===== Master Switch =====
    fun isAdsEnabled(): Boolean {
        return remoteConfig.getBoolean("ads_global_enabled")
    }

    // ===== Banner =====
    fun isBannerEnabled(): Boolean {
        return isAdsEnabled() && remoteConfig.getBoolean("banner_ads")
    }

    // ===== Native =====
    fun isNativeEnabled(): Boolean {
        return isAdsEnabled() && remoteConfig.getBoolean("native_ads")
    }

    // ===== Interstitial =====
    fun isInterstitialEnabled(): Boolean {
        return isAdsEnabled() && remoteConfig.getBoolean("interstitial_enabled")
    }

    fun getInterstitialCooldownMs(): Long {
        val seconds = remoteConfig.getLong("interstitial_cooldown")
        return if (seconds > 0) seconds * 1000 else 60000
    }

    // ===== Rewarded =====
    fun isRewardedEnabled(): Boolean {
        return isAdsEnabled() && remoteConfig.getBoolean("rewarded_enabled")
    }

    // ===== Open Ads =====
    fun isOpenAdsEnabled(): Boolean {
        return isAdsEnabled() && remoteConfig.getBoolean("open_ads_enabled")
    }
}
