package com.runidev.qrcode2025.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.runidev.qrcode2025.ui.fragment.CreateFragment
import com.runidev.qrcode2025.ui.fragment.HistoryFragment
import com.runidev.qrcode2025.ui.fragment.ScannerFragment
import com.runidev.qrcode2025.ui.fragment.SettingsFragment

class HomeViewPaperAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ScannerFragment()
            1 -> CreateFragment()
            2 -> HistoryFragment()
            3 -> SettingsFragment()
            else -> ScannerFragment()
        }
    }
    }