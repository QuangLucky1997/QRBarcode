package com.runidev.qrcode2025.inject
import android.app.Application

import com.google.firebase.analytics.FirebaseAnalytics

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
       // firebaseAnalytics = Firebase.analytics
    }

}