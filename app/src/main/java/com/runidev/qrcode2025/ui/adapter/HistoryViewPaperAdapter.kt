package com.runidev.qrcode2025.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.runidev.qrcode2025.ui.fragment.HistoryCreateFragment
import com.runidev.qrcode2025.ui.fragment.HistoryScanFragment


class HistoryViewPaperAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryScanFragment()
            1 -> HistoryCreateFragment()
            else -> HistoryScanFragment()
        }
    }
}