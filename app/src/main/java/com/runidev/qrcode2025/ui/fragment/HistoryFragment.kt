package com.runidev.qrcode2025.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.runidev.qrcode2025.databinding.FragmentHistoryBinding
import com.runidev.qrcode2025.base.BaseFragment

import com.runidev.qrcode2025.ui.adapter.HistoryViewPaperAdapter
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.ui.viewModel.SharedQrIconViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    private var isCheckBoxVisible = false
    private val adapterHistory by lazy { HistoryViewPaperAdapter(requireActivity()) }
    private val sharedViewModel by viewModels<SharedQrIconViewModel>({ requireActivity() })
    private val qrBarcodeViewModel by viewModels<QrBarcodeViewModel>()
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding
        get() = FragmentHistoryBinding::inflate

    override fun onViewCreated() {
        initView()
        initHandle()
    }

    private fun initHandle() {
        binding.apply {
            imgDelete.setOnClickListener {
                if (sharedViewModel.hasSelectedItems.value == true) {
                    sharedViewModel.requestDelete()
                } else {
                    isCheckBoxVisible = !isCheckBoxVisible
                    sharedViewModel.toggleIcon()
                }
            }

        }
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