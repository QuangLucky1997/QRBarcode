package com.runidev.qrcode2025.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import com.app.devRuni.lockwallpaper.util.FileUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.runidev.qrcode2025.ui.activity.ShowDetailQrActivity
import dagger.hilt.android.qualifiers.ActivityContext
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri

fun Context.changeWallpaper(path: String?, typeSetWall: Int) {
    if (path == null) {
        return
    }
    val wm = WallpaperManager.getInstance(this)
    try {
        val inputStream = FileUtils.openInput(path)
        wm.setStream(inputStream, null, true, typeSetWall)
        inputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun Context.changeWallpaperFull(path: String?) {
    if (path == null) {
        return
    }
    val wm = WallpaperManager.getInstance(this)
    try {
        val inputStream = FileUtils.openInput(path)
        wm.setStream(inputStream)
        inputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}


suspend fun downloadImageSetAs(context: Context, imageUrl: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()

            val input: InputStream = connection.inputStream
            val bitmap: Bitmap = BitmapFactory.decodeStream(input)

            val fileName = "image_${UUID.randomUUID()}.jpg"
            val file = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}

fun stringToTimestamp(dateString: String): Long {
    val formatter = SimpleDateFormat("dd/M/yyyy HH:mm", Locale.getDefault())
    val date = formatter.parse(dateString)
    return date?.time ?: 0
}

fun timestampToString(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd/M/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

fun extractPhoneNumber(input: String): String? {
    val regex = Regex("""SMSTO:(\d+):""")
    val matchResult = regex.find(input)
    return matchResult?.groups?.get(1)?.value
}

fun extractSSID(input: String): String? {
    val regex = Regex("""S:([^;]+);""")
    val matchResult = regex.find(input)
    return matchResult?.groups?.get(1)?.value
}

fun extractEmail(input: String): String? {
    val regex = Regex("""TO:([^;]+);""")
    val matchResult = regex.find(input)
    return matchResult?.groups?.get(1)?.value
}

data class SmsData(val phoneNumber: String, val message: String)

fun parseSmsUri(input: String): SmsData? {
    if (!input.startsWith("SMSTO:")) return null
    val parts = input.removePrefix("SMSTO:").split(":", limit = 2)
    return if (parts.size == 2) {
        SmsData(phoneNumber = parts[0], message = parts[1])
    } else {
        null
    }
}


data class EmailData(val to: String, val subject: String, val body: String)

fun parseMail(input: String): EmailData? {
    if (!input.startsWith("MATMSG:")) return null

    val cleanedInput = input.removePrefix("MATMSG:").removeSuffix(";;")
    val parts = cleanedInput.split(";").map { it.trim() }

    var to = ""
    var subject = ""
    var body = ""

    for (part in parts) {
        when {
            part.startsWith("TO:") -> to = part.removePrefix("TO:")
            part.startsWith("SUB:") -> subject = part.removePrefix("SUB:")
            part.startsWith("BODY:") -> body = part.removePrefix("BODY:")
        }
    }

    return if (to.isNotEmpty()) EmailData(to, subject, body) else null
}

data class WifiData(
    val encryptionType: String,
    val ssid: String,
    val password: String,
    val hidden: Boolean
)

fun parseWifiString(input: String): WifiData? {
    if (!input.startsWith("WIFI:")) return null

    val cleanedInput = input.removePrefix("WIFI:").removeSuffix(";;")
    val parts = cleanedInput.split(";").map { it.trim() }

    var type = ""
    var ssid = ""
    var password = ""
    var hidden = false

    for (part in parts) {
        when {
            part.startsWith("T:") -> type = part.removePrefix("T:")
            part.startsWith("S:") -> ssid = part.removePrefix("S:")
            part.startsWith("P:") -> password = part.removePrefix("P:")
            part.startsWith("H:") -> hidden =
                part.removePrefix("H:").toBooleanStrictOrNull() ?: false
        }
    }

    return if (ssid.isNotEmpty()) WifiData(type, ssid, password, hidden) else null
}

fun sendDataSkipUI(
    context: Context,
    dtTime: String,
    dtTypeQR: String,
    dtQRCode: String,
    activityClass: Class<*>,
    dataQr: String,
    typeDataQr: String,
    dataTimeData: String
) {
    val intentShowQr = Intent(context, activityClass::class.java)
    intentShowQr.putExtra(dataQr, dtQRCode)
    intentShowQr.putExtra(typeDataQr, dtTypeQR)
    intentShowQr.putExtra(dataTimeData, dtTime)
    context.startActivity(intentShowQr)
}

fun generateQRCode(text: String, width: Int = 512, height: Int = 512): Bitmap {
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

@SuppressLint("ObsoleteSdkInt")
fun saveToGallery(context: Context, bitmap: Bitmap, albumName: String) {
    val filename = "${System.currentTimeMillis()}.png"
    val write: (OutputStream) -> Boolean = {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DCIM}/$albumName")
        }

        context.contentResolver.let {
            it.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.let { uri ->
                it.openOutputStream(uri)?.let(write)
            }
        }
    } else {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            .toString() + File.separator + albumName
        val file = File(imagesDir)
        if (!file.exists()) {
            file.mkdir()
        }
        val image = File(imagesDir, filename)
        write(FileOutputStream(image))
    }
}

fun shareImageFromImageView(context: Context, imageView: ImageView) {
    val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap ?: return

    // Lưu bitmap tạm vào cache
    val cachePath = File(context.cacheDir, "shared_images")
    cachePath.mkdirs()
    val file = File(cachePath, "qr_code_shared.png")
    val fos = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    fos.flush()
    fos.close()
    val contentUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, contentUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }


    context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ mã QR qua..."))


}

fun openURL(context: Context, urlData: String) {
    var urlFormat = urlData
    if (!urlFormat.startsWith("http://") && !urlFormat.startsWith("https://")) {
        urlFormat = "http://$urlFormat"
    }
    val browserIntent = Intent(Intent.ACTION_VIEW, urlFormat.toUri())
    context.startActivity(browserIntent)
}

fun regexPhoneNumberAndText(dataSMS: String): Pair<String, String>? {
    val pattern = Regex("^SMSTO:([^:]+):(.+)$")
    val match = pattern.find(dataSMS)
    return if (match != null) {
        val phoneNumber = match.groupValues[1]
        val message = match.groupValues[2]
        Pair(phoneNumber, message)
    } else {
        null
    }
}

 fun openWifiSettings(context: Context) {
    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
    context.startActivity(intent)
}

fun getWifiPasswordOrNull(raw: String): String? {
    return Regex("P:([^;]+)").find(raw)?.groupValues?.get(1)
}





