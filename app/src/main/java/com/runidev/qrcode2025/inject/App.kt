package com.runidev.qrcode2025.inject
import android.app.Application
import com.google.firebase.FirebaseApp

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var app: App
    }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        app = this
        FirebaseApp.initializeApp(this)
        Firebase.messaging.isAutoInitEnabled = true
    }

}