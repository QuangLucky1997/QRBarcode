package com.runidev.qrcode2025.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.repository.QrCodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrBarcodeViewModel @Inject constructor(private val qrCodeRepository: QrCodeRepository) : ViewModel(){
    val allQrCodes: LiveData<List<QrCode>> = qrCodeRepository.allQrCode
    val scannedQrCodes: LiveData<List<QrCode>> = qrCodeRepository.allQrCodeByScan
    val createdQrCodes: LiveData<List<QrCode>> = qrCodeRepository.allQrCodeByCreate



    private val _insertResult = MutableLiveData<Long>()
    val insertResult: LiveData<Long> = _insertResult

    private val _dataExists = MutableLiveData<Boolean>()
    val dataExists: LiveData<Boolean> = _dataExists

    fun insertQrCode(qrCode: QrCode) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = qrCodeRepository.createQrCode(qrCode)
            _insertResult.postValue(result)
        }
    }

    fun checkQrCodeExists(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = qrCodeRepository.checkIfExists(value)
            _dataExists.postValue(count > 0)
        }
    }
    fun deleteQrCode(id:Long){
        viewModelScope.launch(Dispatchers.IO) {
            qrCodeRepository.deleteQrCodeByID(id)
        }
    }
}
