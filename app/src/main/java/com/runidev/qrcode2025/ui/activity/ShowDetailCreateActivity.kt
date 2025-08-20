package com.runidev.qrcode2025.ui.activity

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast


import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.adsBase.AdLayoutType
import com.runidev.qrcode2025.adsBase.BaseNativeAdsHelper
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.databinding.ActivityShowQrCreateBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.generateQRCode
import com.runidev.qrcode2025.util.saveToGallery
import com.runidev.qrcode2025.util.shareImageFromImageView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowDetailCreateActivity :
    BaseActivity<ActivityShowQrCreateBinding>(ActivityShowQrCreateBinding::inflate) {
    companion object {
        const val dataQrCreate = "DATA_CREATE"
        const val typeQrCreate = "TYPE_CREATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = getColor(R.color.blue3)
        initShowData()
        initHandleFunc()
        BaseNativeAdsHelper.loadNativeAd(
            context = this,
            adUnitId = "ca-app-pub-3940256099942544/2247696110",
            layoutType = AdLayoutType.MEDIUM,
            container = binding.viewAds
        )
    }

    private fun initHandleFunc() {
        binding.apply {
            backImg.clicks {
                finish()
            }
            closeImg.clicks {
                val intent = Intent(this@ShowDetailCreateActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra("selected_tab", 1)
                startActivity(intent)
            }
            cardSave.clicks {
                val bitmap = (imgShowQrCode.drawable as? BitmapDrawable)?.bitmap
                if (bitmap != null) {
                    saveToGallery(this@ShowDetailCreateActivity, bitmap, "IMG")
                    Toast.makeText(
                        this@ShowDetailCreateActivity,
                        "Save QrCode Image Success!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            cardShare.clicks {
                shareImageFromImageView(this@ShowDetailCreateActivity, imgShowQrCode)
            }
        }
    }

    private fun initShowData() {
        val dataTypeQr = intent.getStringExtra(typeQrCreate)
        val dataQr = intent.getStringExtra(dataQrCreate)
        binding.apply {
            when (dataTypeQr) {
                QRType.TEXT.name -> {
                    imgIconQr.setImageResource(R.drawable.text_qr)
                }

                QRType.URL.name -> {
                    imgIconQr.setImageResource(R.drawable.url)
                }

                QRType.SMS.name -> {
                    imgIconQr.setImageResource(R.drawable.sendsms)
                }

                QRType.EMAIL.name -> {
                    imgIconQr.setImageResource(R.drawable.sendemail)
                }

                QRType.PHONE.name -> {
                    imgIconQr.setImageResource(R.drawable.baseline_phone_24)
                }

                QRType.CLIPBOARD.name -> {
                    imgIconQr.setImageResource(R.drawable.clipboard)
                    typeQRCreate.text = QRType.CLIPBOARD.name
                }
                QRType.FACEBOOK.name->{
                    imgIconQr.setImageResource(R.drawable.faceb)
                    typeQRCreate.text = QRType.FACEBOOK.name
                }
                QRType.YOUTUBE.name->{
                    imgIconQr.setImageResource(R.drawable.ytb)
                    typeQRCreate.text = QRType.YOUTUBE.name
                }
                QRType.VIBER.name->{
                    imgIconQr.setImageResource(R.drawable.viber)
                    typeQRCreate.text = QRType.VIBER.name
                }

                QRType.INSTAGRAM.name->{
                    imgIconQr.setImageResource(R.drawable.instagram)
                    typeQRCreate.text = QRType.INSTAGRAM.name
                }

                QRType.SPOTIFY.name->{
                    imgIconQr.setImageResource(R.drawable.spotify)
                    typeQRCreate.text = QRType.SPOTIFY.name
                }
                QRType.TWITTER.name->{
                    imgIconQr.setImageResource(R.drawable.x)
                    typeQRCreate.text = QRType.TWITTER.name
                }

            }
            dataQrCreate.text = dataQr
            imgShowQrCode.setImageBitmap(dataQr?.let { generateQRCode(it) })
        }
    }


}