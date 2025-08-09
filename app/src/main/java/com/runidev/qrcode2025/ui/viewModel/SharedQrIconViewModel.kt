package com.runidev.qrcode2025.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.runidev.qrcode2025.modelRoom.QrCode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedQrIconViewModel @Inject constructor() : ViewModel() {
    private val _isNewIconEnabled = MutableLiveData(false)
    val isNewIconEnabled: LiveData<Boolean> get() = _isNewIconEnabled



    fun toggleIcon() {
        _isNewIconEnabled.value = !(_isNewIconEnabled.value ?: false)
    }

    private val _hasSelectedItems = MutableLiveData(false)
    val hasSelectedItems: LiveData<Boolean> get() = _hasSelectedItems

    fun updateHasSelectedItems(hasSelected: Boolean) {
        _hasSelectedItems.value = hasSelected
    }

    private val _deleteRequested = MutableLiveData<Unit>()
    val deleteRequested: LiveData<Unit> get() = _deleteRequested

    fun requestDelete() {
        _deleteRequested.value = Unit
    }
}