package com.runidev.qrcode2025.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.mlkit.vision.barcode.common.Barcode
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseAdapter
import com.runidev.qrcode2025.databinding.ItemListScanQrHistoryBinding
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.util.extractEmail
import com.runidev.qrcode2025.util.extractPhoneNumber
import com.runidev.qrcode2025.util.extractSSID
import javax.annotation.meta.When
import javax.inject.Inject

class QrCodeByScanAdapter @Inject constructor() :
    BaseAdapter<QrCode, ItemListScanQrHistoryBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ItemListScanQrHistoryBinding
        get() = ItemListScanQrHistoryBinding::inflate

    override fun bindItem(
        item: QrCode,
        binding: ItemListScanQrHistoryBinding,
        position: Int
    ) {
        binding.apply {
            when (item.typeQrCode.name) {
                "URL" -> textValueQr.text = item.valueQrCode
                "TEXT" -> textValueQr.text =item.valueQrCode
                "SMS" -> {
                    textValueQr.text = extractPhoneNumber(item.valueQrCode)
                }
                "WIFI" ->{
                    textValueQr.text = extractSSID(item.valueQrCode)
                }
                "EMAIL" -> {
                    textValueQr.text = extractEmail(item.valueQrCode)
                }
                "PHONE" -> item.valueQrCode
                else -> null
            }
            textTypeQr.text = item.typeQrCode.name
            Glide.with(root.context).load(item.iconQRType).into(imgItemQrType)
        }
    }


}