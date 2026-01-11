package com.runidev.qrcode2025.adsBase

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var lastShownTime: Long = 0

    companion object {
        private const val TAG = "InterstitialAdManager"
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    }

    init {
        loadAd()
    }
    private fun loadAd() {
        if (isLoading || interstitialAd != null) return
        if (!RemoteConfigManager.isInterstitialEnabled()) {
            Log.d(TAG, "Interstitial disabled by RemoteConfig")
            return
        }

        isLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                    Log.d(TAG, "Interstitial loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                    Log.e(TAG, "Failed to load interstitial: ${error.message}")
                }
            }
        )
    }

    /** 🔹 Check cooldown */
    private fun canShowAd(): Boolean {
        val now = System.currentTimeMillis()
        val cooldown = RemoteConfigManager.getInterstitialCooldownMs()
        return (now - lastShownTime) >= cooldown
    }

    /** 🔹 Show quảng cáo */
    fun showAd(activity: Activity, onAdClosed: () -> Unit) {
        if (!RemoteConfigManager.isInterstitialEnabled()) {
            Log.d(TAG, "Interstitial disabled → skip show")
            onAdClosed()
            return
        }

        if (interstitialAd != null && canShowAd()) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    lastShownTime = System.currentTimeMillis()
                    loadAd()
                    onAdClosed()
                    Log.d(TAG, "Interstitial dismissed")
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    interstitialAd = null
                    loadAd()
                    onAdClosed()
                    Log.e(TAG, "Interstitial failed to show: ${error.message}")
                }

                override fun onAdShowedFullScreenContent() {
                    interstitialAd = null
                    Log.d(TAG, "Interstitial showed")
                }
            }
            interstitialAd?.show(activity)
        } else {
            Log.d(TAG, "Interstitial not ready or cooldown not finished")
            onAdClosed()
            if (interstitialAd == null) loadAd()
        }
    }
}