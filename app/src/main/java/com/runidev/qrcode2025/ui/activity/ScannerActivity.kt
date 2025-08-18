package com.runidev.qrcode2025.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.databinding.ActivityScannerBinding
import com.runidev.qrcode2025.enumData.QRType
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class ScannerActivity : BaseActivity<ActivityScannerBinding>(ActivityScannerBinding::inflate) {

    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var cameraExecutor: ExecutorService

    @Inject
    lateinit var qrCodeService: QrCodeService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        cameraExecutor = Executors.newSingleThreadExecutor()
        barcodeScanner = BarcodeScanning.getClient()
        requestCameraPermission()
    }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                showPermissionDeniedMessage()
            }
        }


    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                showPermissionRationale()
            }

            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                   // it.setAnalyzer(cameraExecutor, ::analyzeImage)
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Timber.tag("Main123").e(e, "Error start camera")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("UnsafeOptInUsageError")
//    private fun analyzeImage(imageProxy: ImageProxy) {
//        val mediaImage = imageProxy.image
//
//        if (mediaImage != null) {
//            val inputImage = InputImage.fromMediaImage(
//                mediaImage,
//                imageProxy.imageInfo.rotationDegrees
//            )
//            barcodeScanner.process(inputImage)
//                .addOnSuccessListener { barcodes ->
//                    barcodes.forEach { barcode ->
//                        when (barcode.valueType) {
//                            Barcode.TYPE_URL -> {
//                                if (qrCodeService.checkIfDataExistsQrCode(barcode.rawValue) > 0) {
//                                    Timber.tag("Main123").e("QQ")
//                                } else {
//                                    val qrData = QrCode(
//                                        0,
//                                        QRType.URL,
//                                        timestampToString(System.currentTimeMillis()),
//                                        barcode.rawValue.toString()
//                                    )
//                                    qrCodeService.createQrCode(qrData)
//                                }
//                                sendDataSkipUI(timestampToString(System.currentTimeMillis()),"URL",barcode.rawValue.toString())
//                            }
//
//                            Barcode.TYPE_TEXT -> {
//                                if (qrCodeService.checkIfDataExistsQrCode(barcode.rawValue) > 0) {
//                                    Timber.tag("Main123").e("QQ")
//                                } else {
//                                    val qrData = QrCode(
//                                        0,
//                                        QRType.TEXT,
//                                        timestampToString(System.currentTimeMillis()),
//                                        barcode.rawValue.toString()
//                                    )
//                                    qrCodeService.createQrCode(qrData)
//                                }
//                                sendDataSkipUI(timestampToString(System.currentTimeMillis()),"TEXT",barcode.rawValue.toString())
//                            }
//
//                            Barcode.TYPE_SMS -> {
//                                if (qrCodeService.checkIfDataExistsQrCode(barcode.rawValue) > 0) {
//                                    Timber.tag("Main123").e("QQ")
//                                } else {
//                                    val qrData = QrCode(
//                                        0,
//                                        QRType.SMS,
//                                        timestampToString(System.currentTimeMillis()),
//                                        barcode.rawValue.toString()
//                                    )
//                                    qrCodeService.createQrCode(qrData)
//                                }
//                                sendDataSkipUI(timestampToString(System.currentTimeMillis()),"SMS",barcode.rawValue.toString())
//                            }
//
//                            Barcode.TYPE_WIFI -> {
//                                if (qrCodeService.checkIfDataExistsQrCode(barcode.rawValue) > 0) {
//                                    Timber.tag("Main123").e("QQ")
//                                } else {
//                                    val qrData = QrCode(
//                                        0,
//                                        QRType.WIFI,
//                                        timestampToString(System.currentTimeMillis()),
//                                        barcode.rawValue.toString()
//                                    )
//                                    qrCodeService.createQrCode(qrData)
//                                }
//                                sendDataSkipUI(timestampToString(System.currentTimeMillis()),"WIFI",barcode.rawValue.toString())
//                            }
//
//                            Barcode.TYPE_GEO -> {
//                                if (qrCodeService.checkIfDataExistsQrCode(barcode.rawValue) > 0) {
//                                    Timber.tag("Main123").e("QQ")
//                                } else {
//                                    val qrData = QrCode(
//                                        0,
//                                        QRType.GEO,
//                                        timestampToString(System.currentTimeMillis()),
//                                        barcode.rawValue.toString()
//                                    )
//                                    qrCodeService.createQrCode(qrData)
//                                }
//                                sendDataSkipUI(timestampToString(System.currentTimeMillis()),"GEO",barcode.rawValue.toString())
//                            }
//
//                            Barcode.TYPE_EMAIL -> {
//                                if (qrCodeService.checkIfDataExistsQrCode(barcode.rawValue) > 0) {
//                                    Timber.tag("Main123").e("QQ")
//                                } else {
//                                    val qrData = QrCode(
//                                        0,
//                                        QRType.EMAIL,
//                                        timestampToString(System.currentTimeMillis()),
//                                        barcode.rawValue.toString()
//                                    )
//                                    qrCodeService.createQrCode(qrData)
//                                }
//                                sendDataSkipUI(timestampToString(System.currentTimeMillis()),"EMAIL",barcode.rawValue.toString())
//                            }
//                        }
//                    }
//                }
//                .addOnFailureListener { e ->
//                    Timber.tag("Main123").e(e, "Barcode scanning failed")
//                }
//                .addOnCompleteListener {
//                    imageProxy.close()
//                }
//        } else {
//            imageProxy.close()
//        }
//    }


    private fun showPermissionDeniedMessage() {
        Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
    }


    private fun showPermissionRationale() {

    }

    private fun sendDataSkipUI(dtTime:String, dtTypeQR:String,dtQRCode:String) {
        val intentShowQr = Intent(this, ShowDetailQrActivity::class.java)
        intentShowQr.putExtra(ShowDetailQrActivity.getDataQr, dtQRCode)
        intentShowQr.putExtra(ShowDetailQrActivity.typeDataQR, dtTypeQR)
        intentShowQr.putExtra(
            ShowDetailQrActivity.dataTime,
            dtTime
        )
        startActivity(intentShowQr)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        barcodeScanner.close()
    }
}