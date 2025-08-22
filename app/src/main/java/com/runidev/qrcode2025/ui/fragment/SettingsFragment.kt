package com.runidev.qrcode2025.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.runidev.qrcode2025.BuildConfig
import com.runidev.qrcode2025.databinding.FragmentSettingBinding
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.language.LanguageActivity
import com.runidev.qrcode2025.util.ext.clicks
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingBinding>() {
//    @Inject
//    lateinit var preferences: Preferences
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingBinding
        get() = FragmentSettingBinding::inflate

    override fun onViewCreated() {
        initSetData()
        initHandle()
    }

    @SuppressLint("SetTextI18n")
    private fun initSetData() {
        binding.apply {
            switchBeep.isChecked = preferences.isBeep.get()
            switchAutoCopy.isChecked = preferences.isAutoCopy.get()
            switchVibrate.isChecked = preferences.isVibrate.get()
            textDataEngine.text = preferences.dataSearch.get()
            textLicense.text = "Version ${BuildConfig.VERSION_NAME}"
        }
    }

    private fun initHandle() {
        binding.apply {
            switchBeep.setOnCheckedChangeListener { _, isChecked ->
                preferences.isBeep.set(isChecked)
            }
            switchAutoCopy.setOnCheckedChangeListener { _, isChecked ->
                preferences.isAutoCopy.set(isChecked)
            }
            switchVibrate.setOnCheckedChangeListener { _, isChecked ->
                preferences.isVibrate.set(isChecked)
            }
            settingSearch.clicks {
                showSearchEngineDialog(requireActivity())
            }
            settingLanguage.clicks {
                startActivity(
                    Intent(requireActivity(), LanguageActivity::class.java)
                )
            }
        }
    }

    private fun showSearchEngineDialog(context: Context) {
        val searchEngines = arrayOf("Google", "Bing", "Yahoo")
        val savedIndex = preferences.positionSearchEngine.get()
        AlertDialog.Builder(context)
            .setSingleChoiceItems(searchEngines, savedIndex) { dialog, which ->
                val chosen = searchEngines[which]
                preferences.positionSearchEngine.set(which)
                preferences.dataSearch.set(chosen)
                binding.textDataEngine.text = chosen
                dialog.dismiss()
            }
            .show()
    }
}