package com.runidev.qrcode2025.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.databinding.FragmentCreateHistoryBinding
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.activity.ShowDetailCreateActivity
import com.runidev.qrcode2025.ui.activity.ShowDetailQrActivity
import com.runidev.qrcode2025.ui.adapter.QrCodeByScanAdapter
import com.runidev.qrcode2025.ui.dialog.DialogConfirmDelete
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.ui.viewModel.SharedQrIconViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryCreateFragment : BaseFragment<FragmentCreateHistoryBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateHistoryBinding
        get() = FragmentCreateHistoryBinding::inflate

    private val qrBarcodeViewModel by viewModels<QrBarcodeViewModel>()

    @Inject
    lateinit var dialogConfirmDelete: DialogConfirmDelete
    private val sharedViewModel by viewModels<SharedQrIconViewModel>({ requireActivity() })

    @Inject
    lateinit var adapterHistoryScan: QrCodeByScanAdapter
    override fun onViewCreated() {
        setupRecyclerView()
        observeQrCodeData()
        observeSharedViewModel()
        observeDeleteRequest()
    }


    private fun setupRecyclerView() {
        binding.rvCreateHistory.adapter = adapterHistoryScan
        adapterHistoryScan.onSelectionChanged = { selected ->
            sharedViewModel.updateHasSelectedItems(selected.isNotEmpty())
        }

        adapterHistoryScan.subjectDetailQr = { qrCode ->
            startDetailQrScreen(qrCode)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeQrCodeData() {
        qrBarcodeViewModel.createdQrCodes.observe(viewLifecycleOwner) { qrCodes ->
            if (qrCodes.isNotEmpty()) {
                binding.groupViewNoData.isGone = true
                adapterHistoryScan.data = qrCodes.toMutableList()
                adapterHistoryScan.notifyDataSetChanged()
            } else {
                binding.groupViewNoData.isGone = false
                adapterHistoryScan.data.clear()
                adapterHistoryScan.notifyDataSetChanged()
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
        val intentDetail = Intent(requireActivity(), ShowDetailCreateActivity::class.java).apply {
            putExtra(ShowDetailCreateActivity.dataQrCreate, itemQr.valueQrCode)
            putExtra(ShowDetailCreateActivity.typeQrCreate, itemQr.typeQrCode.name)
        }
        startActivity(intentDetail)
    }
}