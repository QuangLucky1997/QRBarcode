package com.runidev.qrcode2025.ui.activity


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.databinding.ActivityClipboardBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.enumData.QrCreate
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.sendDataSkipUI
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ClipboardActivity :
    BaseActivity<ActivityClipboardBinding>(ActivityClipboardBinding::inflate) {
    private val createQrViewModel: QrBarcodeViewModel by viewModels()


    @Inject
    lateinit var qrCodeService: QrCodeService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        handleCreateQR()
    }

    private fun handleCreateQR() {
        binding.apply {
            tabCreate.clicks {
                val dataClipBoard = edtClipBoard.text.toString().isNotEmpty()
                if (dataClipBoard) {
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(edtClipBoard.text.toString())
                    if (checkExistData == 0) {
                        val clipboardModel = QrCode(
                            0, QRType.TEXT, timestampToString(System.currentTimeMillis()),
                            edtClipBoard.text.toString(), false, R.drawable.text_qr, false
                        )
                        createQrViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@ClipboardActivity, ShowDetailCreateActivity::class.java)
                        intent.putExtra(
                            ShowDetailCreateActivity.dataQrCreate,
                            edtClipBoard.text.toString()
                        )
                        intent.putExtra(
                            ShowDetailCreateActivity.typeQrCreate,
                            QRType.CLIPBOARD.name
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@ClipboardActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@ClipboardActivity,
                        "Data is not empty!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            backImg.clicks {
                finish()
            }


        }
    }
}