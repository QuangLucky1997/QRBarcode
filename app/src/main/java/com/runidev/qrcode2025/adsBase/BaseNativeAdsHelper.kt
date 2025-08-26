package com.runidev.qrcode2025.adsBase

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.runidev.qrcode2025.R

object BaseNativeAdsHelper {

    private var currentNativeAd: NativeAd? = null

    fun loadNativeAd(
        context: Context,
        adUnitId: String,
        layoutType: AdLayoutType,
        container: ConstraintLayout
    ) {
        // 🔹 Check config từ Firebase
        if (!RemoteConfigManager.isNativeEnabled()) {
            container.removeAllViews()
            container.visibility = View.GONE
            Log.d("NativeAds", "Native ads disabled by RemoteConfig")
            return
        }

        val builder = AdLoader.Builder(context, adUnitId)

        builder.forNativeAd { nativeAd ->
            // Hủy ads cũ nếu có để tránh memory leak
            currentNativeAd?.destroy()
            currentNativeAd = nativeAd

            // Inflate layout theo type
            val adView = when (layoutType) {
                AdLayoutType.SMALL -> LayoutInflater.from(context)
                    .inflate(R.layout.custom_native_small_ads, container, false) as NativeAdView
                AdLayoutType.MEDIUM -> LayoutInflater.from(context)
                    .inflate(R.layout.custom_medium_ads, container, false) as NativeAdView
            }

            // Bind dữ liệu
            populateNativeAdView(nativeAd, adView)

            // Gắn vào container
            container.removeAllViews()
            container.addView(adView)
            container.visibility = View.VISIBLE
        }

        val adLoader = builder
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("NativeAds", "Failed to load: ${adError.message}")
                    container.removeAllViews()
                    container.visibility = View.GONE
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Headline
        adView.findViewById<TextView>(R.id.ad_headline)?.apply {
            text = nativeAd.headline
            adView.headlineView = this
        }

        // Body
        adView.findViewById<TextView?>(R.id.ad_body)?.let {
            if (nativeAd.body.isNullOrEmpty()) {
                it.visibility = View.GONE
            } else {
                it.text = nativeAd.body
                it.visibility = View.VISIBLE
            }
            adView.bodyView = it
        }

        // Icon
        adView.findViewById<ImageView?>(R.id.ad_app_icon)?.let {
            val icon = nativeAd.icon
            if (icon == null) {
                it.visibility = View.GONE
            } else {
                it.setImageDrawable(icon.drawable)
                it.visibility = View.VISIBLE
            }
            adView.iconView = it
        }

        // Advertiser
        adView.findViewById<TextView?>(R.id.ad_advertiser)?.let {
            if (nativeAd.advertiser.isNullOrEmpty()) {
                it.visibility = View.GONE
            } else {
                it.text = nativeAd.advertiser
                it.visibility = View.VISIBLE
            }
            adView.advertiserView = it
        }

        // CTA
        adView.findViewById<Button?>(R.id.ad_call_to_action)?.let {
            if (nativeAd.callToAction.isNullOrEmpty()) {
                it.visibility = View.GONE
            } else {
                it.text = nativeAd.callToAction
                it.visibility = View.VISIBLE
            }
            adView.callToActionView = it
        }

        // Media
        adView.findViewById<MediaView?>(R.id.ad_media)?.let { mediaView ->
            mediaView.mediaContent = nativeAd.mediaContent
            adView.mediaView = mediaView
        }

        adView.setNativeAd(nativeAd)
    }

    fun destroy() {
        currentNativeAd?.destroy()
        currentNativeAd = null
    }
}

/**
 * Enum chọn loại layout
 */
enum class AdLayoutType {
    SMALL,
    MEDIUM
}
