package com.runidev.qrcode2025.ui.activity


import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.activity.viewModels
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.databinding.ActivityTextBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TextActivity : BaseActivity<ActivityTextBinding>(ActivityTextBinding::inflate){
    private val createQrViewModel: QrBarcodeViewModel by viewModels()
    @Inject
    lateinit var qrCodeService: QrCodeService
    override fun onCreateView() {
        super.onCreateView()
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        initHandleEvent()
    }

    private fun initHandleEvent() {
        binding.apply {
            tabCreate.clicks {
                val dataClipBoard = edtText.text.toString().isNotEmpty()
                if (dataClipBoard) {
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(edtText.text.toString())
                    if (checkExistData == 0) {
                        val clipboardModel = QrCode(
                            0, QRType.TEXT, timestampToString(System.currentTimeMillis()),
                            edtText.text.toString(), false, R.drawable.text_qr, false
                        )
                        createQrViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@TextActivity, ShowDetailCreateActivity::class.java)
                        intent.putExtra(
                            ShowDetailCreateActivity.dataQrCreate,
                            edtText.text.toString()
                        )
                        intent.putExtra(
                            ShowDetailCreateActivity.typeQrCreate,
                            QRType.TEXT.name
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@TextActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@TextActivity,
                        "Data is not empty!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
}