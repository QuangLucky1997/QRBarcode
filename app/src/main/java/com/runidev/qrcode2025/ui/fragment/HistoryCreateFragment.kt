package com.runidev.qrcode2025.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.databinding.FragmentCreateHistoryBinding

class HistoryCreateFragment : BaseFragment<FragmentCreateHistoryBinding> (){
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateHistoryBinding
        get() = FragmentCreateHistoryBinding::inflate

    override fun onViewCreated() {

    }
}