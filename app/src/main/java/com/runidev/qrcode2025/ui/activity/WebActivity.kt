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
import com.runidev.qrcode2025.databinding.ActivityUrlBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WebActivity : BaseActivity<ActivityUrlBinding>(ActivityUrlBinding::inflate) {
    @Inject
    lateinit var qrCodeService: QrCodeService
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
                val dataUrl = edtUrl.text.toString().isNotEmpty()
                if (dataUrl ) {
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(edtUrl.text.toString())
                    if (checkExistData == 0) {
                        val dataUrl = edtUrl.text.toString()
                        val clipboardModel = QrCode(
                            0, QRType.URL, timestampToString(System.currentTimeMillis()),
                            dataUrl, false, R.drawable.url, false
                        )
                        createQrViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@WebActivity, ShowDetailCreateActivity::class.java)
                        intent.putExtra(
                            ShowDetailCreateActivity.dataQrCreate,
                            dataUrl
                        )
                        intent.putExtra(
                            ShowDetailCreateActivity.typeQrCreate,
                            QRType.URL.name
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@WebActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@WebActivity,
                        "Data is not empty!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            edtUrl.addTextChangedListener(object : TextWatcher {
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

        }




    }
}