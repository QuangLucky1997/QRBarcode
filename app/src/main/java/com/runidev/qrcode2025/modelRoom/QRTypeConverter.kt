package com.runidev.qrcode2025.modelRoom

import androidx.room.TypeConverter
import com.runidev.qrcode2025.helper.QRType

class QRTypeConverter {
    @TypeConverter
    fun fromQRType(type: QRType): String {
        return type.name
    }

    @TypeConverter
    fun toQRType(name: String): QRType {
        return QRType.valueOf(name)
    }
}