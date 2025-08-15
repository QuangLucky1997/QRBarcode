package com.runidev.qrcode2025.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import com.google.android.gms.common.wrappers.Wrappers.packageManager

object ContactUtils {
    fun sendSmsWithIntent(phoneNumber: String?, message: String?, context: Context) {
        val smsUri = "smsto:$phoneNumber".toUri()
        val intent = Intent(Intent.ACTION_SENDTO, smsUri)
        intent.putExtra("sms_body", message)
        context.startActivity(intent)
    }


//    }

    fun pickPhoneNumber(
        activity: Activity,
        launcher: ActivityResultLauncher<Intent>,
        onPhoneSelected: (String?) -> Unit
    ) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        launcher.launch(intent)
    }


    fun getPhoneNumberFromUri(context: Context, uri: Uri): String? {
        var phoneNumber: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if (phoneIndex != -1) {
                    phoneNumber = it.getString(phoneIndex)
                }
            }
        }
        return phoneNumber
    }



    @SuppressLint("QueryPermissionsNeeded", "IntentReset")
    fun sendEmail(subject: String, message: String, context: Context) {
        val mIntent = Intent(Intent.ACTION_SEND).apply {
            data = "mailto:".toUri()
            type = "message/rfc822"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        try {
            if (mIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mIntent)
            } else {
                Toast.makeText(context, "No Email App Found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun copyToClipboard(context: Context, text: String, label: String = "Copied Text") {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Data copied to clipboard: $text", Toast.LENGTH_SHORT).show()
    }

    fun sendSMS(phoneNumber: String?, message: String?, context: Context) {
        val phone = phoneNumber
        val body  = message
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "smsto:$phone".toUri()
            putExtra("sms_body", body)
        }
        context.startActivity(intent)
    }


    fun sendEmailViaGmail(context: Context, to: String, subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            setPackage("com.google.android.gm") // ép mở Gmail
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Can not open Gmail", Toast.LENGTH_LONG).show()
        }
    }
}

