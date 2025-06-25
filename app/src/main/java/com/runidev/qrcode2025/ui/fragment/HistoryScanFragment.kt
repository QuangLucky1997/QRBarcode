package com.runidev.qrcode2025.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.databinding.FragmentScanHistoryBinding
import com.runidev.qrcode2025.ui.adapter.QrCodeByScanAdapter
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryScanFragment : BaseFragment<FragmentScanHistoryBinding>() {
    private val qrBarcodeViewModel by viewModels<QrBarcodeViewModel>()
    @Inject
    lateinit var adapterHistoryScan: QrCodeByScanAdapter
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanHistoryBinding
        get() = FragmentScanHistoryBinding::inflate

    override fun onViewCreated() {
        initShowData()
    }

    private fun initShowData() {
        qrBarcodeViewModel.scannedQrCodes.observe(viewLifecycleOwner) { qrCodes ->
            if(qrCodes.isNotEmpty())
            {
                adapterHistoryScan.data = qrCodes as MutableList
                binding.rvScanHistory.adapter = adapterHistoryScan
            }

        }
    }
}