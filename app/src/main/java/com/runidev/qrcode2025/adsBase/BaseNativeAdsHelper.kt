package com.runidev.qrcode2025.adsBase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
        container: androidx.constraintlayout.widget.ConstraintLayout
    ) {
        val builder = AdLoader.Builder(context, adUnitId)

        builder.forNativeAd { nativeAd ->
            // Hủy ads cũ nếu có
            currentNativeAd?.destroy()
            currentNativeAd = nativeAd

            // Inflate layout theo type
            val adView = when (layoutType) {
                AdLayoutType.SMALL -> LayoutInflater.from(context)
                    .inflate(R.layout.custom_native_small_ads, null) as NativeAdView
                AdLayoutType.MEDIUM -> LayoutInflater.from(context)
                    .inflate(R.layout.custom_medium_ads, null) as NativeAdView
            }

            // Bind dữ liệu
            populateNativeAdView(nativeAd, adView)

            // Gắn vào container
            container.removeAllViews()
            container.addView(adView)
        }

        val adLoader = builder
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // TODO: Handle lỗi nếu cần
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

        // Body (nếu có)
        adView.findViewById<TextView?>(R.id.ad_body)?.let {
            if (nativeAd.body == null) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.text = nativeAd.body
            }
            adView.bodyView = it
        }

        // Icon
        adView.findViewById<ImageView?>(R.id.ad_app_icon)?.let {
            val icon = nativeAd.icon
            if (icon == null) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.setImageDrawable(icon.drawable)
            }
            adView.iconView = it
        }

        // Advertiser
        adView.findViewById<TextView?>(R.id.ad_advertiser)?.let {
            if (nativeAd.advertiser == null) {
                it.visibility = View.GONE
            } else {
                it.text = nativeAd.advertiser
                it.visibility = View.VISIBLE
            }
            adView.advertiserView = it
        }

        // CTA Button
        adView.findViewById<Button?>(R.id.ad_call_to_action)?.let {
            if (nativeAd.callToAction == null) {
                it.visibility = View.GONE
            } else {
                it.text = nativeAd.callToAction
                it.visibility = View.VISIBLE
            }
            adView.callToActionView = it
        }

        // Media (medium layout mới có)
        adView.findViewById<MediaView?>(R.id.ad_media)?.let { mediaView ->
//            if (nativeAd.mediaContent != null) {
//                it.setImageDrawable(nativeAd.mediaContent?.mainImage)
//                it.visibility = View.VISIBLE
//            } else {
//                it.visibility = View.GONE
//            }
//            adView.mediaView = null // optional

            adView.mediaView = mediaView
            mediaView.mediaContent = nativeAd.mediaContent
        }

        adView.setNativeAd(nativeAd)
    }
}

/**
 * Enum chọn loại layout
 */
enum class AdLayoutType {
    SMALL,
    MEDIUM
}