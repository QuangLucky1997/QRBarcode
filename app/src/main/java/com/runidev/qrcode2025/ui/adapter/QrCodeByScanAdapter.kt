package com.runidev.qrcode2025.ui.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseAdapter
import com.runidev.qrcode2025.databinding.ItemListScanQrHistoryBinding
import com.runidev.qrcode2025.modelRoom.QrCode


import javax.inject.Inject

class QrCodeByScanAdapter @Inject constructor() :
    BaseAdapter<QrCode, ItemListScanQrHistoryBinding>() {

    var subjectDetailQr: ((QrCode) -> Unit)? = null
    var onSelectionChanged: ((List<QrCode>) -> Unit)? = null

    private val selectedItems = mutableSetOf<QrCode>()
    private var isDeleteMode = false

    fun getSelectedItems(): List<QrCode> = selectedItems.toList()

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelectedItems() {
        selectedItems.clear()
        notifyDataSetChanged()
        onSelectionChanged?.invoke(selectedItems.toList())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUseNewIcon(enabled: Boolean) {
        isDeleteMode = enabled
        if (!enabled) {
            selectedItems.clear()
        }
        notifyDataSetChanged()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ItemListScanQrHistoryBinding
        get() = ItemListScanQrHistoryBinding::inflate

    override fun bindItem(item: QrCode, binding: ItemListScanQrHistoryBinding, position: Int) {
        binding.apply {
            textValueQr.text = item.valueQrCode
            textTypeQr.text = item.typeQrCode.name
            Glide.with(root.context).load(item.iconQRType).into(imgItemQrType)

            imgDeleteOrDetail.setImageResource(
                if (isDeleteMode && selectedItems.contains(item)) R.drawable.checked
                else if (isDeleteMode) R.drawable.uncheck
                else R.drawable.right_arrow
            )

            viewItemQrScanHistory.setOnClickListener {
                if (isDeleteMode) {
                    if (selectedItems.contains(item)) {
                        selectedItems.remove(item)
                    } else {
                        selectedItems.add(item)
                    }

                    notifyItemChanged(position)
                    onSelectionChanged?.invoke(selectedItems.toList())
                } else {
                    subjectDetailQr?.invoke(item)
                }
            }
        }
    }
}