package com.runidev.qrcode2025.ui.activity

import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.activity.viewModels
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.databinding.ActivityViberBinding
import com.runidev.qrcode2025.helper.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViberActivity : BaseActivity<ActivityViberBinding>(ActivityViberBinding::inflate) {
    private val createQrViewModel: QrBarcodeViewModel by viewModels()

    @Inject
    lateinit var qrCodeService: QrCodeService
    override fun onCreateView() {
        super.onCreateView()
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        initHandle()

    }

    private fun initHandle() {
        binding.apply {
            tabCreate.clicks {
                val dataClipBoard = editNumberPhone.text.toString().isNotEmpty()
                if (dataClipBoard) {
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(editNumberPhone.text.toString())
                    if (checkExistData == 0) {
                        val clipboardModel = QrCode(
                            0,
                            QRType.VIBER,
                            timestampToString(System.currentTimeMillis()),
                            countryCode.selectedCountryCode() + "-${editNumberPhone.text}",
                            false,
                            R.drawable.viber,
                            false
                        )
                        createQrViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@ViberActivity, ShowDetailCreateActivity::class.java)
                        intent.putExtra(
                            ShowDetailCreateActivity.dataQrCreate,
                            editNumberPhone.text.toString()
                        )
                        intent.putExtra(
                            ShowDetailCreateActivity.typeQrCreate,
                            QRType.VIBER.name
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@ViberActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@ViberActivity,
                        "Data is not empty!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}