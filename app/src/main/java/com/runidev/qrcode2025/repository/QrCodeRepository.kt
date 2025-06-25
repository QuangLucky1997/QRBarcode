package com.runidev.qrcode2025.repository

import androidx.lifecycle.LiveData
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.modelRoom.QrCode
import javax.inject.Inject

class QrCodeRepository @Inject constructor(private val qrCodeService: QrCodeService) {
    val allQrCode: LiveData<List<QrCode>> = qrCodeService.getAllQRCode()
    val allQrCodeByScan: LiveData<List<QrCode>> = qrCodeService.getAllQRScan()
    val allQrCodeByCreate: LiveData<List<QrCode>> = qrCodeService.getAllQRCreate()


    suspend fun createQrCode(qrCode: QrCode): Long {
        return qrCodeService.createQrCode(qrCode)
    }
    suspend fun checkIfExists(data: String): Int {
        return qrCodeService.checkIfDataExistsQrCode(data)
    }
}