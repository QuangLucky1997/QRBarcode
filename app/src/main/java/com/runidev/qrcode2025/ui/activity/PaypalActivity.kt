package com.runidev.qrcode2025.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isGone
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.databinding.ActivityClipboardBinding
import com.runidev.qrcode2025.databinding.ActivityPaypalBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaypalActivity : BaseActivity<ActivityPaypalBinding>(ActivityPaypalBinding::inflate) {

    private val createQrViewModel: QrBarcodeViewModel by viewModels()
    @Inject
    lateinit var qrCodeService: QrCodeService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        initChangeView()
        initHandleEvent()
    }


    private fun initChangeView() {
        binding.apply {
            viewUrl.clicks {
                cardLinkMe.isGone = true
                cardUserNameMe.isGone = false
            }
            viewFacebook.clicks {
                cardLinkMe.isGone = false
                cardUserNameMe.isGone = true
            }
            bacKImg.clicks {
                finish()
            }
        }
    }

    private fun initHandleEvent() {
        binding.apply {
            tabCreate.clicks {
                val dataClipBoard = edtPaypal.text.toString().isNotEmpty()
                if (dataClipBoard) {
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(edtPaypal.text.toString())
                    if (checkExistData == 0) {
                        val clipboardModel = QrCode(
                            0, QRType.PAYPAL, timestampToString(System.currentTimeMillis()),
                            edtPaypal.text.toString(), false, R.drawable.paypal, false
                        )
                        createQrViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@PaypalActivity, ShowDetailCreateActivity::class.java)
                        intent.putExtra(
                            ShowDetailCreateActivity.dataQrCreate,
                            edtPaypal.text.toString()
                        )
                        intent.putExtra(
                            ShowDetailCreateActivity.typeQrCreate,
                            QRType.PAYPAL.name
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@PaypalActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@PaypalActivity,
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