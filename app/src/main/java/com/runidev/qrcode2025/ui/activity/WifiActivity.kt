package com.runidev.qrcode2025.ui.activity

import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isGone
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.databinding.ActivityWifiBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WifiActivity : BaseActivity<ActivityWifiBinding>(ActivityWifiBinding::inflate) {
    @Inject
    lateinit var qrCodeService: QrCodeService
    private val qrCodeViewModel by viewModels<QrBarcodeViewModel>()
    var typePassWifi = 0
    var dataWifiQr = ""
    override fun onCreateView() {
        super.onCreateView()
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        initChangeView()
        handleFunc()
    }

    private fun initChangeView() {
        binding.apply {
            viewWPA.clicks {
                cardWep.isGone = true
                cardNone.isGone = true
                cardWpa.isGone = false
                edtPass.isGone = false
                viewLine.isGone = false
                typePassWifi = 0
            }
            viewWep.clicks {
                cardWep.isGone = false
                cardNone.isGone = true
                cardWpa.isGone = true
                edtPass.isGone = false
                viewLine.isGone = false
                typePassWifi = 1
            }
            viewNone.clicks {
                cardWep.isGone = true
                cardNone.isGone = false
                cardWpa.isGone = true
                edtPass.isGone = true
                viewLine.isGone = true
                typePassWifi = 2

            }
        }
    }

    private fun handleFunc() {
        binding.apply {
            tabCreate.clicks {
                val dataNameWifi = edtNameWifi.text.toString().isNotEmpty()
                val dataPass = edtPass.text.toString().isNotEmpty()
                if (dataNameWifi && dataPass) {
                    when (typePassWifi) {
                        0 -> {
                            dataWifiQr = "WIFI:T:WPA;S:${edtNameWifi.text};P:${edtPass.text};;"
                        }

                        1 -> {
                            dataWifiQr = "WIFI:T:WEP;S:${edtNameWifi.text};P:${edtPass.text};;"
                        }

                        2 -> {
                            dataWifiQr = "WIFI:T:nopass;S:${edtNameWifi.text};;"
                        }
                    }
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(edtNameWifi.text.toString())
                    if (checkExistData == 0) {
                        val clipboardModel = QrCode(
                            0, QRType.WIFI, timestampToString(System.currentTimeMillis()),
                            dataWifiQr, false, R.drawable.connectwifi, false
                        )
                        qrCodeViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@WifiActivity, ShowDetailCreateActivity::class.java)
                        intent.putExtra(
                            ShowDetailCreateActivity.dataQrCreate,
                            dataWifiQr
                        )
                        intent.putExtra(
                            ShowDetailCreateActivity.typeQrCreate,
                            QRType.WIFI.name
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@WifiActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@WifiActivity,
                        "Data is not empty!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            bacKImg.clicks {
                finish()
            }

        }


    }


}


