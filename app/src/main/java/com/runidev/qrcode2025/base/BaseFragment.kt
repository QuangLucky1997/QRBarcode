package com.runidev.qrcode2025.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<V : ViewBinding> : Fragment() {

    lateinit var binding: V

    abstract val _binding:(LayoutInflater, ViewGroup?, Boolean) -> V

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = _binding(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    abstract fun onViewCreated()
}