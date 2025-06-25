package com.runidev.qrcode2025.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.runidev.qrcode2025.databinding.FragmentHistoryBinding
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.ui.adapter.HistoryViewPaperAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {
    private val adapterHistory by lazy { HistoryViewPaperAdapter(requireActivity()) }
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding
        get() = FragmentHistoryBinding::inflate

    override fun onViewCreated() {
         initView()
    }

    private fun initView() {
        binding.viewPaper.adapter = adapterHistory
        binding.tabLayoutHistory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        TabLayoutMediator(binding.tabLayoutHistory, binding.viewPaper) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Scan"
                }

                1 -> {
                    tab.text = "Create"
                }


            }
        }.attach()
    }
}