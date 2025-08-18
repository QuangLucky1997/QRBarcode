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
import com.runidev.qrcode2025.databinding.ActivityInstagramBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InstagramActivity  : BaseActivity<ActivityInstagramBinding>(ActivityInstagramBinding::inflate){

    @Inject lateinit var qrCodeService: QrCodeService
    private val qrCodeViewModel : QrBarcodeViewModel by viewModels()
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
                cardLoginName.isGone = true
                cardUrl.isGone = false
            }
            viewLoginName.clicks {
                cardLoginName.isGone = false
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
                val dataClipBoard = edtInstagram.text.toString().isNotEmpty()
                if (dataClipBoard) {
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(edtInstagram.text.toString())
                    if (checkExistData == 0) {
                        val clipboardModel = QrCode(
                            0, QRType.INSTAGRAM, timestampToString(System.currentTimeMillis()),
                            edtInstagram.text.toString(), false, R.drawable.instagram, false
                        )
                        qrCodeViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@InstagramActivity, ShowDetailCreateActivity::class.java)
                        intent.putExtra(
                            ShowDetailCreateActivity.dataQrCreate,
                            edtInstagram.text.toString()
                        )
                        intent.putExtra(
                            ShowDetailCreateActivity.typeQrCreate,
                            QRType.INSTAGRAM.name
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@InstagramActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@InstagramActivity,
                        "Data is not empty!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
}