package com.runidev.qrcode2025.modelRoom


import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.runidev.qrcode2025.enumData.QRType
import java.util.Date

@Entity
data class QrCode(
    @PrimaryKey(autoGenerate = true) var idQrCode: Long,
    @ColumnInfo(name = "TypeQr") val typeQrCode: QRType,
    @ColumnInfo(name = "TimeCreateQr") val dateTimeQrCode: String,
    @ColumnInfo(name = "ValueQRCode") val valueQrCode: String = "",
    @ColumnInfo(name = "isScan") val isScan: Boolean = false,
    @ColumnInfo(name = "iconQrType") val iconQRType: Int,
    @ColumnInfo(name = "isChecked") var isChecked: Boolean = false
)