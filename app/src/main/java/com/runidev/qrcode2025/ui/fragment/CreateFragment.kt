package com.runidev.qrcode2025.ui.fragment

import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.LayoutInflater
import android.view.ViewGroup
import com.runidev.qrcode2025.databinding.FragmentCreateBinding
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.enumData.QrCreate
import com.runidev.qrcode2025.ui.activity.ClipboardActivity
import com.runidev.qrcode2025.ui.activity.FacebookActivity
import com.runidev.qrcode2025.ui.activity.HomeActivity
import com.runidev.qrcode2025.ui.activity.InstagramActivity
import com.runidev.qrcode2025.ui.activity.PaypalActivity
import com.runidev.qrcode2025.ui.activity.PhoneActivity
import com.runidev.qrcode2025.ui.activity.SMSActivity
import com.runidev.qrcode2025.ui.activity.ScannerActivity
import com.runidev.qrcode2025.ui.activity.ShowDetailQrActivity
import com.runidev.qrcode2025.ui.activity.SpotifyActivity
import com.runidev.qrcode2025.ui.activity.TextActivity
import com.runidev.qrcode2025.ui.activity.ViberActivity
import com.runidev.qrcode2025.ui.activity.WebActivity
import com.runidev.qrcode2025.ui.activity.WifiActivity
import com.runidev.qrcode2025.ui.activity.XActivity
import com.runidev.qrcode2025.ui.activity.YoutubeActivity
import com.runidev.qrcode2025.ui.adapter.ListCreateTypeQrAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CreateFragment : BaseFragment<FragmentCreateBinding>() {
    @Inject
    lateinit var adapterQrCreate: ListCreateTypeQrAdapter
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateBinding
        get() = FragmentCreateBinding::inflate

    override fun onViewCreated() {
        initData()
    }

    private fun initData() {
        val qrFunctionList: MutableList<QrCreate> = QrCreate.entries.toMutableList()
        adapterQrCreate.data = qrFunctionList
        binding.rvAllCreateQr.adapter = adapterQrCreate
        adapterQrCreate.subjectCreateQr = { data ->
            when (data.label) {
                "Clipboard" -> {
                    moveActivity(ClipboardActivity())
                }

                "Website" -> moveActivity(WebActivity())
                "Facebook" -> moveActivity(FacebookActivity())
                "Youtube" -> moveActivity(YoutubeActivity())
                "Paypal" -> moveActivity(PaypalActivity())
                "Instagram" -> moveActivity(InstagramActivity())
                "Twitter" -> moveActivity(XActivity())
                "SMS" -> moveActivity(SMSActivity())
                "Wi-Fi" -> moveActivity(WifiActivity())
                "Spotify" -> moveActivity(SpotifyActivity())
                "Text" -> moveActivity(TextActivity())
                "Phone" -> moveActivity(PhoneActivity())
                "Viber" -> moveActivity(ViberActivity())
            }
        }
    }

    private fun moveActivity(activity: Activity) {
        val intentActivity = Intent(requireActivity(), activity::class.java)
        startActivity(intentActivity)
    }


}