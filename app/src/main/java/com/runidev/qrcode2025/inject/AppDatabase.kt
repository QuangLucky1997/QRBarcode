package com.runidev.qrcode2025.inject

import androidx.room.Database
import androidx.room.RoomDatabase
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.modelRoom.QrCode

@Database(
    entities = [QrCode::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): QrCodeService
}