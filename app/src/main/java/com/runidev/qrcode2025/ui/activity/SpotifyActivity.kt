package com.runidev.qrcode2025.ui.activity

import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.databinding.ActivitySpotifyBinding
import com.runidev.qrcode2025.helper.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SpotifyActivity
    : BaseActivity<ActivitySpotifyBinding>(ActivitySpotifyBinding::inflate) {
    @Inject
    lateinit var qrCodeService: QrCodeService
    private val createQrViewModel: QrBarcodeViewModel by viewModels()
    override fun onCreateView() {
        super.onCreateView()
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        handleCreateQR()
    }

    private fun handleCreateQR() {
        binding.apply {
            tabCreate.clicks {
                val dataNameArtist = edtNameArtist.text.toString().isNotEmpty()
                val dataNameSong = edtNameSong.text.toString().isNotEmpty()
                if (dataNameArtist && dataNameSong) {
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(edtNameSong.text.toString())
                    if (checkExistData == 0) {
                        val dataSMS = edtNameArtist.text.toString() + "-${edtNameSong.text}"
                        val clipboardModel = QrCode(
                            0, QRType.SMS, timestampToString(System.currentTimeMillis()),
                            dataSMS, false, R.drawable.sendsms, false
                        )
                        createQrViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@SpotifyActivity, ShowDetailCreateActivity::class.java)
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
                            this@SpotifyActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@SpotifyActivity,
                        "Data is not empty!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            edtNameArtist.addTextChangedListener(object : TextWatcher {
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

            edtNameSong.addTextChangedListener(object : TextWatcher {
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