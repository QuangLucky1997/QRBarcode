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
import com.runidev.qrcode2025.databinding.ActivityFacebBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FacebookActivity : BaseActivity<ActivityFacebBinding>(ActivityFacebBinding::inflate) {
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
                cardFacebook.isGone = true
                cardUrl.isGone = false
            }
            viewFacebook.clicks {
                cardFacebook.isGone = false
                cardUrl.isGone = true
            }
            bacKImg.clicks {
                finish()
            }
        }
    }

    private fun initHandleEvent() {
        binding.apply {
            tabCreate.clicks {
                val dataClipBoard = edtFacebookIdLayout.text.toString().isNotEmpty()
                if (dataClipBoard) {
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(edtFacebookIdLayout.text.toString())
                    if (checkExistData == 0) {
                        val clipboardModel = QrCode(
                            0, QRType.FACEBOOK, timestampToString(System.currentTimeMillis()),
                            edtFacebookIdLayout.text.toString(), false, R.drawable.faceb, false
                        )
                        createQrViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@FacebookActivity, ShowDetailCreateActivity::class.java)
                        intent.putExtra(
                            ShowDetailCreateActivity.dataQrCreate,
                            edtFacebookIdLayout.text.toString()
                        )
                        intent.putExtra(
                            ShowDetailCreateActivity.typeQrCreate,
                            QRType.FACEBOOK.name
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@FacebookActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@FacebookActivity,
                        "Data is not empty!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
}