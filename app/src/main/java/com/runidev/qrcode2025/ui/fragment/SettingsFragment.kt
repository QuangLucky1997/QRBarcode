package com.runidev.qrcode2025.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.runidev.qrcode2025.databinding.FragmentSettingBinding
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.helper.Preferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingBinding>() {
    @Inject lateinit var preferences: Preferences
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingBinding
        get() = FragmentSettingBinding::inflate

    override fun onViewCreated() {
        initSetData()
        initHandle()
    }

    private fun initSetData() {
        binding.apply {
            switchBeep.isChecked = preferences.isBeep.get()
            switchAutoCopy.isChecked = preferences.isAutoCopy.get()
            switchVibrate.isChecked = preferences.isVibrate.get()
        }
    }

    private fun initHandle() {
        binding.apply {
            switchBeep.setOnCheckedChangeListener { _, isChecked ->
                preferences.isBeep.set(isChecked)
            }
            switchAutoCopy.setOnCheckedChangeListener { _, isChecked ->
                preferences.isAutoCopy.set(isChecked)
            }
            switchVibrate.setOnCheckedChangeListener { _, isChecked ->
                preferences.isVibrate.set(isChecked)
            }
        }
    }
}