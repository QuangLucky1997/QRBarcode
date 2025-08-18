package com.runidev.qrcode2025.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isGone
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.databinding.ActivityYtbBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class YoutubeActivity : BaseActivity<ActivityYtbBinding>(ActivityYtbBinding::inflate) {
    @Inject
    lateinit var qrCodeService: QrCodeService
    private val createQrViewModel: QrBarcodeViewModel by viewModels()
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
                cardIDVideo.isGone = true
                cardChannelID.isGone = true
                cardUrl.isGone = false
                edtYtbIdLayout.hint = "Enter URL"
            }
            viewIDVideo.clicks {
                cardIDVideo.isGone = false
                cardChannelID.isGone = true
                cardUrl.isGone = true
                edtYtbIdLayout.hint = "Enter ID Video"
            }
            viewChannelID.clicks {
                cardIDVideo.isGone = true
                cardChannelID.isGone = false
                cardUrl.isGone = true
                edtYtbIdLayout.hint = "Enter ChannelID"
            }
        }
    }

    private fun initHandleEvent() {
        binding.apply {
            tabCreate.clicks {
                val dataClipBoard = edtYtbIdLayout.text.toString().isNotEmpty()
                if (dataClipBoard) {
                    val checkExistData =
                        qrCodeService.checkIfDataExistsQrCode(edtYtbIdLayout.text.toString())
                    if (checkExistData == 0) {
                        val clipboardModel = QrCode(
                            0, QRType.TEXT, timestampToString(System.currentTimeMillis()),
                            edtYtbIdLayout.text.toString(), false, R.drawable.ytb, false
                        )
                        createQrViewModel.insertQrCode(clipboardModel)
                        val intent =
                            Intent(this@YoutubeActivity, ShowDetailCreateActivity::class.java)
                        intent.putExtra(
                            ShowDetailCreateActivity.dataQrCreate,
                            edtYtbIdLayout.text.toString()
                        )
                        intent.putExtra(
                            ShowDetailCreateActivity.typeQrCreate,
                            QRType.YOUTUBE.name
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@YoutubeActivity,
                            "Data already exists !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@YoutubeActivity,
                        "Data is not empty!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            edtYtbIdLayout.addTextChangedListener(object : TextWatcher {
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