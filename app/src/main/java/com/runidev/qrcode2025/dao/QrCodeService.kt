package com.runidev.qrcode2025.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.runidev.qrcode2025.modelRoom.QrCode
import kotlinx.coroutines.flow.Flow

@Dao
interface QrCodeService {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createQrCode(qrCode: QrCode): Long

    @Query("SELECT * FROM QrCode")
    fun getAllQRCode(): LiveData<List<QrCode>>

    @Query("SELECT COUNT(*) FROM QrCode WHERE  valueQrCode = :checkData")
    fun checkIfDataExistsQrCode(checkData: String?): Int

    @Query("SELECT * FROM QrCode WHERE isScan = 1")
    fun getAllQRScan(): LiveData<List<QrCode>>

    @Query("SELECT * FROM QrCode WHERE isScan = 0")
    fun getAllQRCreate(): LiveData<List<QrCode>>

    @Query("DELETE FROM QrCode WHERE idQrCode = :qrId")
    fun deleteById(qrId: Int)

}