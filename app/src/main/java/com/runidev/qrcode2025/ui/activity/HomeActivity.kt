package com.runidev.qrcode2025.ui.activity


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.OnNewIntentProvider

import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning

import com.runidev.qrcode2025.R

import com.runidev.qrcode2025.databinding.ActivityHomeBinding
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.ui.adapter.HomeViewPaperAdapter

import com.runidev.qrcode2025.ui.fragment.ScannerFragment
import com.runidev.qrcode2025.ui.fragment.SettingsFragment
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.modelRoom.QrCode

import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString

import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    private val homeViewPaperAdapter by lazy { HomeViewPaperAdapter(this) }
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val permissions = arrayOf(Manifest.permission.CAMERA)

    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var cameraExecutor: ExecutorService


    @Inject
    lateinit var qrCodeService: QrCodeService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        cameraExecutor = Executors.newSingleThreadExecutor()
        barcodeScanner = BarcodeScanning.getClient()
        initView()
        initHandle()
        checkCameraPermission()
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {
            val selectedTab = it.getIntExtra("selected_tab", 0)
            binding.viewPaper.setCurrentItem(selectedTab, false)
        }
    }



    private fun initView() {
        binding.viewPaper.adapter = homeViewPaperAdapter
        binding.viewPaper.setUserInputEnabled(false)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.setTint(ContextCompat.getColor(this@HomeActivity, R.color.blue2))
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.setTint(ContextCompat.getColor(this@HomeActivity, R.color.black))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        TabLayoutMediator(binding.tabLayout, binding.viewPaper) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Scan"
                    tab.setIcon(R.drawable.scan1)
                }

                1 -> {
                    tab.text = "Create"
                    tab.setIcon(R.drawable.create1)
                }

                2 -> {
                    tab.text = "History"
                    tab.setIcon(R.drawable.history1)
                }

                3 -> {
                    tab.text = "Setting"
                    tab.setIcon(R.drawable.setting1)
                }
            }
        }.attach()
    }

    private fun initHandle() {

    }


    fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                permissions,
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                supportFragmentManager.fragments.forEach { fragment ->
                    if (fragment is ScannerFragment) {
                        fragment.onCameraPermissionGranted()
                    }
                }
            } else {
                Toast.makeText(this, "You need permission scan QR code", Toast.LENGTH_LONG).show()
            }
        }
    }


}