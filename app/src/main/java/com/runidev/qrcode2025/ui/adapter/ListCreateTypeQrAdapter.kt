package com.runidev.qrcode2025.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.runidev.qrcode2025.base.BaseAdapter
import com.runidev.qrcode2025.databinding.ItemListCreateQrBinding
import com.runidev.qrcode2025.enumData.QrCreate
import com.runidev.qrcode2025.util.ext.clicks
import javax.inject.Inject

class ListCreateTypeQrAdapter @Inject constructor() :
    BaseAdapter<QrCreate, ItemListCreateQrBinding>() {
     var subjectCreateQr: ((QrCreate) -> Unit)? = null


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ItemListCreateQrBinding
        get() = ItemListCreateQrBinding::inflate

    override fun bindItem(
        item: QrCreate,
        binding: ItemListCreateQrBinding,
        position: Int
    ) {
        binding.apply {
            Glide.with(root.context).load(item.iconRes).into(imgIconQr)
            nameQrCreate.text = item.label
            viewCreateQR.clicks {
                subjectCreateQr?.invoke(item)
            }
        }
    }


}