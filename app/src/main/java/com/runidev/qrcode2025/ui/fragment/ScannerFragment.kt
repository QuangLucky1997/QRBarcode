package com.runidev.qrcode2025.ui.fragment

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.databinding.FramentScannerBinding
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.dao.QrCodeService
import com.runidev.qrcode2025.helper.QRType
import com.runidev.qrcode2025.modelRoom.QrCode
import com.runidev.qrcode2025.ui.activity.HomeActivity
import com.runidev.qrcode2025.ui.activity.ShowDetailQrActivity
import com.runidev.qrcode2025.ui.viewModel.QrBarcodeViewModel
import com.runidev.qrcode2025.util.timestampToString
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class ScannerFragment : BaseFragment<FramentScannerBinding>() {

    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var cameraExecutor: ExecutorService
    private var isCameraInitialized = false
    private val qrcodeViewModel: QrBarcodeViewModel by viewModels()

    @Inject
    lateinit var qrCodeService: QrCodeService

    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FramentScannerBinding
        get() = FramentScannerBinding::inflate

    override fun onViewCreated() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        barcodeScanner = BarcodeScanning.getClient()
        if (hasCameraPermission()) {
            startCamera()
            startAnimation()
        } else {
            requestCameraPermission()
        }
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().also {
                    it.setAnalyzer(cameraExecutor, ::analyzeImage)
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner, cameraSelector, preview, imageAnalysis
                )
            } catch (e: Exception) {
                Timber.tag("Main123").e(e, "Error start camera")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    fun startAnimation() {
        binding.scanFrame.post {
            val frameHeight = binding.scanFrame.height
            val lineHeight = binding.scanLine.height
            val animator = ValueAnimator.ofFloat(0f, (frameHeight - lineHeight).toFloat()).apply {
                duration = 2000
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener {
                    val value = it.animatedValue as Float
                    binding.scanLine.translationY = value
                }
            }
            animator.start()
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun analyzeImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(
                mediaImage, imageProxy.imageInfo.rotationDegrees
            )
            barcodeScanner.process(inputImage).addOnSuccessListener { barcodes ->
                barcodes.forEach { barcode ->
                    processBarcode(barcode)
                }
            }.addOnFailureListener { e ->
                Timber.tag("Main123").e(e, "Barcode scanning failed")
            }.addOnCompleteListener {
                imageProxy.close()

            }

        } else {
            imageProxy.close()
        }
    }

    private fun processBarcode(barcode: Barcode) {
        val rawValue = barcode.rawValue ?: return
        val qrType = mapBarcodeTypeToQrType(barcode.valueType) ?: return
        val qrIconType = mapBarcodeTypeToQrIconType(barcode.valueType) ?: return

        if (qrCodeService.checkIfDataExistsQrCode(rawValue) > 0) {
            Timber.tag("Main123").e("Duplicate QR code: $rawValue")
        } else {
            val qrData = QrCode(
                0,
                qrType,
                timestampToString(System.currentTimeMillis()),
                rawValue,
                isScan = true,
                qrIconType
            )
            qrcodeViewModel.insertQrCode(qrData)

        }
    }

    private fun mapBarcodeTypeToQrType(valueType: Int): QRType? {
        return when (valueType) {
            Barcode.TYPE_URL -> QRType.URL
            Barcode.TYPE_TEXT -> QRType.TEXT
            Barcode.TYPE_SMS -> QRType.SMS
            Barcode.TYPE_WIFI -> QRType.WIFI
            Barcode.TYPE_GEO -> QRType.GEO
            Barcode.TYPE_EMAIL -> QRType.EMAIL
            Barcode.TYPE_PHONE -> QRType.PHONE
            else -> null
        }
    }

    private fun mapBarcodeTypeToQrIconType(valueType: Int): Int? {
        return when (valueType) {
            Barcode.TYPE_URL -> R.drawable.url
            Barcode.TYPE_TEXT -> R.drawable.text_qr
            Barcode.TYPE_SMS -> R.drawable.sendsms
            Barcode.TYPE_WIFI -> R.drawable.connectwifi
            Barcode.TYPE_GEO -> R.drawable.ic_flag_german
            Barcode.TYPE_EMAIL -> R.drawable.sendemail
            Barcode.TYPE_PHONE -> R.drawable.baseline_phone_24
            else -> null
        }
    }


    private fun sendDataSkipUI(dtTypeQR: String, dtQRCode: String) {
        val intentShowQr = Intent(requireContext(), ShowDetailQrActivity::class.java)
        intentShowQr.putExtra(ShowDetailQrActivity.getDataQr, dtQRCode)
        intentShowQr.putExtra(ShowDetailQrActivity.typeDataQR, dtTypeQR)
        startActivity(intentShowQr)
    }

    fun onCameraPermissionGranted() {
        if (isAdded && !isCameraInitialized) {
            startCamera()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        barcodeScanner.close()
    }

    private fun hasCameraPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
                startAnimation()
            } else {
                Toast.makeText(requireContext(), "Permission camera is denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }


}