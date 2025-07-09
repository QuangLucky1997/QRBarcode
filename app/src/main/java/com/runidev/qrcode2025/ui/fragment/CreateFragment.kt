package com.runidev.qrcode2025.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.runidev.qrcode2025.databinding.FragmentCreateBinding
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.helper.QrCreate
import com.runidev.qrcode2025.ui.adapter.ListCreateTypeQrAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CreateFragment : BaseFragment<FragmentCreateBinding>() {
    @Inject lateinit var adapterQrCreate: ListCreateTypeQrAdapter
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateBinding
        get() = FragmentCreateBinding::inflate

    override fun onViewCreated() {
        initData()
    }

    private fun initData() {
        val qrFunctionList: MutableList<QrCreate> = QrCreate.values().toMutableList()
        adapterQrCreate.data = qrFunctionList
        binding.rvAllCreateQr.adapter = adapterQrCreate
    }
}