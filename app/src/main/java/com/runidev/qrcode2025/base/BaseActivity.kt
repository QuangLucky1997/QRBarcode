package com.runidev.qrcode2025.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


open class BaseActivity<VB : ViewBinding>(
    val bindingInflater: (LayoutInflater) -> VB
) : AppCompatActivity() {
    val binding: VB by lazy { bindingInflater(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onCreateView()
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
    }


    open fun onCreateView() {

    }



}
