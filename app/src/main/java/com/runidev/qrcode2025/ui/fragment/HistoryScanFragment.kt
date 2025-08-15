package com.runidev.qrcode2025.ui.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.databinding.FragmentScanHistoryBinding
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.activity.ShowDetailQrActivity
import com.runidev.qrcode2025.ui.adapter.QrCodeByScanAdapter
import com.runidev.qrcode2025.ui.dialog.DialogConfirmDelete
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.ui.viewModel.SharedQrIconViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryScanFragment : BaseFragment<FragmentScanHistoryBinding>() {

    private val qrBarcodeViewModel by viewModels<QrBarcodeViewModel>()

    @Inject
    lateinit var dialogConfirmDelete: DialogConfirmDelete

    private val sharedViewModel by viewModels<SharedQrIconViewModel>({ requireActivity() })

    @Inject
    lateinit var adapterHistoryScan: QrCodeByScanAdapter

    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScanHistoryBinding
        get() = FragmentScanHistoryBinding::inflate

    override fun onViewCreated() {
        setupRecyclerView()
        observeSharedViewModel()
        observeQrCodeData()
        observeDeleteRequest()
    }

    private fun setupRecyclerView() {
        binding.rvScanHistory.adapter = adapterHistoryScan
        adapterHistoryScan.onSelectionChanged = { selected ->
            sharedViewModel.updateHasSelectedItems(selected.isNotEmpty())
        }

        adapterHistoryScan.subjectDetailQr = { qrCode ->
            startDetailQrScreen(qrCode)
        }
    }

    private fun observeQrCodeData() {
        qrBarcodeViewModel.scannedQrCodes.observe(viewLifecycleOwner) { qrCodes ->
            if (qrCodes.isNotEmpty()) {
                binding.groupViewNoData.isGone = true
                adapterHistoryScan.data = qrCodes.toMutableList()
            } else {
                binding.groupViewNoData.isGone = false
            }
        }
    }

    private fun observeSharedViewModel() {
        sharedViewModel.isNewIconEnabled.observe(viewLifecycleOwner) { isEnabled ->
            adapterHistoryScan.setUseNewIcon(isEnabled)
        }
    }

    private fun observeDeleteRequest() {
        sharedViewModel.deleteRequested.observe(viewLifecycleOwner) {
            if (adapterHistoryScan.getSelectedItems().isNotEmpty()) {
                showConfirmDeleteDialog()
            }
        }
    }

    private fun showConfirmDeleteDialog() {
        dialogConfirmDelete.show(requireActivity().supportFragmentManager, "confirm_delete")
        dialogConfirmDelete.actionDelete = {
            val selectedItems = adapterHistoryScan.getSelectedItems()
            for (dataID in selectedItems) {
                qrBarcodeViewModel.deleteQrCode(dataID.idQrCode)
            }
            adapterHistoryScan.clearSelectedItems()
            sharedViewModel.updateHasSelectedItems(false)
            sharedViewModel.toggleIcon()
        }
    }

    private fun startDetailQrScreen(itemQr: QrCode) {
        val intentDetail = Intent(requireActivity(), ShowDetailQrActivity::class.java).apply {
            putExtra(ShowDetailQrActivity.getDataQr, itemQr.valueQrCode)
            putExtra(ShowDetailQrActivity.typeDataQR, itemQr.typeQrCode.name)
        }
        startActivity(intentDetail)
    }
}