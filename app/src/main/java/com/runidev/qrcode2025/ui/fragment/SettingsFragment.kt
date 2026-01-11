package com.runidev.qrcode2025.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.runidev.qrcode2025.BuildConfig
import com.runidev.qrcode2025.databinding.FragmentSettingBinding
import com.runidev.qrcode2025.base.BaseFragment
import com.runidev.qrcode2025.language.LanguageActivity
import com.runidev.qrcode2025.util.ext.clicks
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri
import com.runidev.qrcode2025.helper.Preferences
import com.runidev.qrcode2025.ui.activity.FeedbackActivity
import com.runidev.qrcode2025.util.ConstantLocal.Constants
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingBinding>() {
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
            settingPrivacy.clicks {
                val url = Constants.LINK_POLICY

                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            settingFeedback.clicks {
                startActivity(Intent(requireActivity(), FeedbackActivity::class.java))
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