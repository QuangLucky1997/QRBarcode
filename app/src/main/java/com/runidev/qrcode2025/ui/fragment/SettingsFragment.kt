package com.runidev.qrcode2025.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.runidev.qrcode2025.databinding.FragmentSettingBinding
import com.runidev.qrcode2025.base.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingBinding
        get() = FragmentSettingBinding::inflate

    override fun onViewCreated() {

    }
}