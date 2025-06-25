package com.runidev.qrcode2025.Context

import android.app.Activity
import androidx.appcompat.app.AlertDialog

fun Activity.showAlertPermission(callback: (Boolean) -> Unit) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Permission Required")
    builder.setMessage("Gallery access needed. Go to Android settings, tap permissions, and tap allow.")
    builder.setPositiveButton("GO TO SETTINGS") { dialog, which ->
        callback.invoke(true)
    }
    builder.setNegativeButton("DISMISS") { dialog, which ->
        callback.invoke(false)
    }
    builder.show()
}