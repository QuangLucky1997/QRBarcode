package com.runidev.qrcode2025.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.runidev.qrcode2025.databinding.FragmentCreateBinding
import com.runidev.qrcode2025.base.BaseFragment

class CreateFragment : BaseFragment<FragmentCreateBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateBinding
        get() = FragmentCreateBinding::inflate

    override fun onViewCreated() {
        initData()
    }

    private fun initData() {

    }
}