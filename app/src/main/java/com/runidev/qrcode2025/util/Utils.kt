package com.runidev.qrcode2025.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            part.startsWith("H:") -> hidden = part.removePrefix("H:").toBooleanStrictOrNull() ?: false
        }
    }

    return if (ssid.isNotEmpty()) WifiData(type, ssid, password, hidden) else null
}


