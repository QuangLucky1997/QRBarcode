package com.runidev.qrcode2025.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.databinding.ActivityClipboardBinding
import com.runidev.qrcode2025.databinding.ActivitySmsBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SMSActivity   : BaseActivity<ActivitySmsBinding>(ActivitySmsBinding::inflate){
    @Inject lateinit var qrCodeService: QrCodeService
    private val createQrViewModel: QrBarcodeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        handleCreateQR()
    }


    private fun handleCreateQR() {
        binding.apply {
            tabCreate.clicks {
                val dataPhoneNumber = edtPhone.text.toString().isNotEmpty()
                val dataText = edtMessage.text.toString().isNotEmpty()
                if (dataPhoneNumber && dataText) {
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(edtMessage.text.toString())
                    if (checkExistData == 0) {
                        val dataSMS = edtPhone.text.toString() + "-${edtMessage.text}"
                        val clipboardModel = QrCode(
                            0, QRType.SMS, timestampToString(System.currentTimeMillis()),
                            dataSMS, false, R.drawable.sendsms, false
                        )
                        createQrViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@SMSActivity, ShowDetailCreateActivity::class.java)
                        intent.putExtra(
                            ShowDetailCreateActivity.dataQrCreate,
                           dataSMS
                        )
                        intent.putExtra(
                            ShowDetailCreateActivity.typeQrCreate,
                            QRType.SMS.name
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@SMSActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@SMSActivity,
                        "Data is not empty!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            edtPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrEmpty()) {
                        tabCreate.alpha = 0.3f
                        tabCreate.isClickable = false
                    } else {

                        tabCreate.alpha = 1f
                        tabCreate.isClickable = true
                    }
                }
            })

            edtMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrEmpty()) {
                        tabCreate.alpha = 0.3f
                        tabCreate.isClickable = false
                    } else {

                        tabCreate.alpha = 1f
                        tabCreate.isClickable = true
                    }
                }
            })
                backImg.clicks {
                    finish()
                }
        }






    }
}