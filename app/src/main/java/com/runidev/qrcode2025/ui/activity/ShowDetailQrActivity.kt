package com.runidev.qrcode2025.ui.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.databinding.ActivityDetailQrcodeBinding
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.util.parseMail
import com.runidev.qrcode2025.util.parseSmsUri
import com.runidev.qrcode2025.util.parseWifiString
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.graphics.createBitmap
import com.runidev.qrcode2025.util.ContactUtils
import com.runidev.qrcode2025.util.ext.clicks
import com.runidev.qrcode2025.util.getWifiPasswordOrNull
import com.runidev.qrcode2025.util.openURL
import com.runidev.qrcode2025.util.openWifiSettings
import com.runidev.qrcode2025.util.regexPhoneNumberAndText
import kotlin.toString


@AndroidEntryPoint
class ShowDetailQrActivity :
    BaseActivity<ActivityDetailQrcodeBinding>(ActivityDetailQrcodeBinding::inflate) {

    private val pickContactLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { contactUri ->
                    val phoneNumber = ContactUtils.getPhoneNumberFromUri(this, contactUri)
                    if (phoneNumber != null) {
                        val dataQR = intent.getStringExtra(getDataQr)
                        ContactUtils.sendSmsWithIntent(phoneNumber, dataQR, this)
                    } else {
                        Toast.makeText(this, "Can not get phone number", Toast.LENGTH_SHORT).show()
                    }
                } ?: Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }
        }

    companion object {
        const val getDataQr = "GET_DATA_QR"
        const val typeDataQR = "TYPE_DATA_QR"
        const val dataTime = "DATA_TIME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = getColor(R.color.white01)
        showDataQr()
        initHandle()


    }

    private fun initHandle() {
        val dataQR = intent.getStringExtra(getDataQr)
        binding.apply {
            backBtn.clicks {
                finish()
            }
            shareBtn.clicks {

            }
            viewSendSMS.clicks {
                pickContactNumber()
            }
            viewSendEmail.clicks {
                if (dataQR != null) {
                    ContactUtils.sendEmail("", dataQR, this@ShowDetailQrActivity)
                }

            }
            viewCopyText.clicks {
                if (dataQR != null) {
                    ContactUtils.copyToClipboard(this@ShowDetailQrActivity, dataQR)
                }
            }

            viewOpenUrl.clicks {
                if (dataQR != null) {
                    openURL(this@ShowDetailQrActivity, dataQR)
                }
            }

            viewCopyUrl.clicks {
                if (dataQR != null) {
                    ContactUtils.copyToClipboard(this@ShowDetailQrActivity, dataQR)
                }
            }
            viewSendSMSType.clicks {
                if (dataQR != null) {
                    val phone = regexPhoneNumberAndText(dataQR)?.first
                    val textSend = regexPhoneNumberAndText(dataQR)?.second
                    ContactUtils.sendSMS(phone, textSend, this@ShowDetailQrActivity)
                }
            }
            viewCopySMS.clicks {
                if (dataQR != null) {
                    ContactUtils.copyToClipboard(this@ShowDetailQrActivity, dataQR)
                }
            }
            viewConnectWifi.clicks {
                openWifiSettings(this@ShowDetailQrActivity)
            }
            viewCopyPass.clicks {
                if (dataQR != null) {
                    val checkGetPass = getWifiPasswordOrNull(dataQR)
                    if (checkGetPass == null) {
                        Toast.makeText(this@ShowDetailQrActivity, "Error", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        ContactUtils.copyToClipboard(this@ShowDetailQrActivity, checkGetPass)
                    }
                }

            }
            viewCopyWifi.clicks {
                if (dataQR != null) {
                    ContactUtils.copyToClipboard(this@ShowDetailQrActivity, dataQR)
                }
            }

            viewSendMail.clicks {
                if (dataQR != null) {
                    val emailData = parseMail(dataQR)
                    if (emailData != null) {
                        ContactUtils.sendEmailViaGmail(this@ShowDetailQrActivity,emailData.to,emailData.subject,emailData.body)
                    } else {
                        println("Error!")
                    }
                }
            }
            viewCopyEmail.clicks {
                if (dataQR != null) {
                    ContactUtils.copyToClipboard(this@ShowDetailQrActivity, dataQR)
                }
            }



        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDataQr() {
        val dataQR = intent.getStringExtra(getDataQr)
        val dataTypeQR = intent.getStringExtra(typeDataQR)
        when (dataTypeQR) {
            "URL" -> {
                binding.textDetailQr.text = dataQR
                binding.typeQrImg.setImageResource(R.drawable.url)
                binding.textTypeQr.text = "URL"
                binding.viewTypeTextQr.isGone = true
                binding.viewTypeQrUrl.isGone = false
                binding.viewTypeWifiQr.isGone = true
                binding.viewTypeEmailQR.isGone = true
                binding.viewTypeQrPhone.isGone = true
                binding.viewTypeSMS.isGone = true
                binding.imgShowQrCode.setImageBitmap(generateQRCode(dataQR.toString()))
            }

            "TEXT" -> {
                binding.typeQrImg.setImageResource(R.drawable.text_qr)
                binding.textTypeQr.text = "TEXT"
                binding.textDetailQr.text = dataQR
                binding.viewTypeTextQr.isGone = false
                binding.viewTypeQrUrl.isGone = true
                binding.viewTypeWifiQr.isGone = true
                binding.viewTypeEmailQR.isGone = true
                binding.viewTypeQrPhone.isGone = true
                binding.viewTypeSMS.isGone = true
                binding.imgShowQrCode.setImageBitmap(generateQRCode(dataQR.toString()))
            }

            "SMS" -> {
                val result = parseSmsUri(dataQR.toString())
                if (result != null) {
                    binding.textDetailQr.text =
                        "Phone:${result.phoneNumber}\nMessage:${result.message}"
                } else {
                    println("Error!")
                }
                binding.typeQrImg.setImageResource(R.drawable.sendsms)
                binding.textTypeQr.text = "SMS"
                binding.viewTypeTextQr.isGone = true
                binding.viewTypeSMS.isGone = false
                binding.viewTypeQrUrl.isGone = true
                binding.viewTypeWifiQr.isGone = true
                binding.viewTypeEmailQR.isGone = true
                binding.viewTypeQrPhone.isGone = true
                binding.imgShowQrCode.setImageBitmap(generateQRCode(dataQR.toString()))
            }

            "WIFI" -> {
                val wifiData = parseWifiString(dataQR.toString())
                if (wifiData != null) {
                    binding.textDetailQr.text =
                        "Wifi Name : ${wifiData.ssid} \nTypeSecurity :${wifiData.encryptionType} "
                } else {
                    println("Error!")
                }
                binding.typeQrImg.setImageResource(R.drawable.connectwifi)
                binding.textTypeQr.text = "WIFI"
                binding.viewTypeTextQr.isGone = true
                binding.viewTypeQrUrl.isGone = true
                binding.viewTypeWifiQr.isGone = false
                binding.viewTypeEmailQR.isGone = true
                binding.viewTypeQrPhone.isGone = true
                binding.viewTypeSMS.isGone = true
                binding.imgShowQrCode.setImageBitmap(generateQRCode(dataQR.toString()))
            }

            "PHONE" -> {
                binding.typeQrImg.setImageResource(R.drawable.baseline_phone_24)
                binding.textTypeQr.text = "PHONE"
                binding.textDetailQr.text = dataQR
                binding.viewTypeTextQr.isGone = true
                binding.viewTypeQrUrl.isGone = true
                binding.viewTypeWifiQr.isGone = true
                binding.viewTypeEmailQR.isGone = true
                binding.viewTypeQrPhone.isGone = false
                binding.viewTypeSMS.isGone = true
                binding.imgShowQrCode.setImageBitmap(generateQRCode(dataQR.toString()))
            }

            "EMAIL" -> {
                val emailData = parseMail(dataQR.toString())
                if (emailData != null) {
                    binding.textDetailQr.text =
                        "Email: ${emailData.to}  \nSubject: ${emailData.subject}  \nBody: ${emailData.body} "
                } else {
                    println("Error!")
                }
                binding.typeQrImg.setImageResource(R.drawable.mail)
                binding.textTypeQr.text = "EMAIL"
                binding.viewTypeTextQr.isGone = true
                binding.viewTypeQrUrl.isGone = true
                binding.viewTypeWifiQr.isGone = true
                binding.viewTypeEmailQR.isGone = false
                binding.viewTypeQrPhone.isGone = true
                binding.viewTypeSMS.isGone = true
                binding.imgShowQrCode.setImageBitmap(generateQRCode(dataQR.toString()))
            }

            "GEO" -> {
                binding.typeQrImg.setImageResource(R.drawable.geo)
                binding.textTypeQr.text = "GEO"
                binding.textDetailQr.text = dataQR
            }

        }


    }

    private fun generateQRCode(text: String, width: Int = 512, height: Int = 512): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(
            text,
            BarcodeFormat.QR_CODE,
            width,
            height
        )
        return createBitmap(width, height, Bitmap.Config.RGB_565).apply {
            for (x in 0 until width) {
                for (y in 0 until height) {
                    setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
    }

    private fun pickContactNumber() {
        ContactUtils.pickPhoneNumber(this, pickContactLauncher) { phoneNumber ->
            if (phoneNumber != null) {
            } else {
                Toast.makeText(this, "Can not choose contact", Toast.LENGTH_SHORT).show()
            }
        }
    }


}