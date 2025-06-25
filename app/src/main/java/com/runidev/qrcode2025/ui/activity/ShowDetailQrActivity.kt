package com.runidev.qrcode2025.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.core.view.isGone
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.databinding.ActivityDetailQrcodeBinding
import com.runidev.qrcode2025.helper.lightStatusBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShowDetailQrActivity :
    BaseActivity<ActivityDetailQrcodeBinding>(ActivityDetailQrcodeBinding::inflate) {
    companion object {
        const val getDataQr = "GET_DATA_QR"
        const val typeDataQR = "TYPE_DATA_QR"
        const val dataTime = "DATA_TIME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        showDataQr()
    }

    private fun showDataQr() {
        val dataQR = intent.getStringExtra(getDataQr)
        val dataTypeQR = intent.getStringExtra(typeDataQR)
        when (dataTypeQR) {
            "URL" -> {
                binding.typeQrImg.setImageResource(R.drawable.url)
                binding.textTypeQr.text = "URL"
                binding.textDetailQr.text = dataQR
                binding.viewTypeTextQr.isGone = true
                binding.viewTypeQrUrl.isGone = false
                binding.viewTypeWifiQr.isGone = true
                binding.viewTypeEmailQR.isGone = true
                binding.viewTypeQrPhone.isGone = true
                binding.viewTypeSMS.isGone = true
            }

            "TEXT" -> {
                binding.typeQrImg.setImageResource(R.drawable.text_qr)
                binding.textTypeQr.text = "TEXT"
                binding.textDetailQr.text = dataQR
                binding.viewTypeTextQr.isGone = false
                binding.viewTypeQrUrl.isGone = true
                binding.viewTypeWifiQr.isGone = true
                binding.viewTypeEmailQR.isGone = true
                binding.viewTypeQrPhone.isGone = true
                binding.viewTypeSMS.isGone = true
            }

            "SMS" -> {
                binding.typeQrImg.setImageResource(R.drawable.sendsms)
                binding.textTypeQr.text = "SMS"
                binding.textDetailQr.text = dataQR
                binding.viewTypeTextQr.isGone = true
                binding.viewTypeSMS.isGone = false
                binding.viewTypeQrUrl.isGone = true
                binding.viewTypeWifiQr.isGone = true
                binding.viewTypeEmailQR.isGone = true
                binding.viewTypeQrPhone.isGone = true
            }

            "WIFI" -> {
                binding.typeQrImg.setImageResource(R.drawable.connectwifi)
                binding.textTypeQr.text = "WIFI"
                binding.textDetailQr.text = dataQR
                binding.viewTypeTextQr.isGone = true
                binding.viewTypeQrUrl.isGone = true
                binding.viewTypeWifiQr.isGone = false
                binding.viewTypeEmailQR.isGone = true
                binding.viewTypeQrPhone.isGone = true
                binding.viewTypeSMS.isGone = true
            }

            "PHONE" -> {
                binding.typeQrImg.setImageResource(R.drawable.baseline_phone_24)
                binding.textTypeQr.text = "PHONE"
                binding.textDetailQr.text = dataQR
                binding.viewTypeTextQr.isGone = true
                binding.viewTypeQrUrl.isGone = true
                binding.viewTypeWifiQr.isGone = true
                binding.viewTypeEmailQR.isGone = true
                binding.viewTypeQrPhone.isGone = false
                binding.viewTypeSMS.isGone = true
            }

            "EMAIL" -> {
                binding.typeQrImg.setImageResource(R.drawable.mail)
                binding.textTypeQr.text = "EMAIL"
                binding.textDetailQr.text = dataQR
                binding.viewTypeTextQr.isGone = true
                binding.viewTypeQrUrl.isGone = true
                binding.viewTypeWifiQr.isGone = true
                binding.viewTypeEmailQR.isGone = false
                binding.viewTypeQrPhone.isGone = true
                binding.viewTypeSMS.isGone = true
            }

            "GEO" -> {
                binding.typeQrImg.setImageResource(R.drawable.iconx)
                binding.textTypeQr.text = "GEO"
                binding.textDetailQr.text = dataQR
            }

        }


    }


}