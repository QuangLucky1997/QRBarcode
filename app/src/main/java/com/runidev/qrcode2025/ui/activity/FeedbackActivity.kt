package com.runidev.qrcode2025.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseActivity
import com.runidev.qrcode2025.databinding.ActivityFeedbackBinding
import com.runidev.qrcode2025.enumData.FeedBack
import com.runidev.qrcode2025.helper.lightStatusBar
import com.runidev.qrcode2025.ui.adapter.FeedbackAdapter
import com.runidev.qrcode2025.util.ContactUtils.sendEmailViaGmail
import com.runidev.qrcode2025.util.clicks
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeedbackActivity : BaseActivity<ActivityFeedbackBinding>(ActivityFeedbackBinding::inflate) {
    @Inject
    lateinit var adapterFeedback: FeedbackAdapter
    private var listChooseData = arrayListOf<String>()
    private var dataFeedback = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        initSetData()
        initHandleSubmit()
    }

    private fun initHandleSubmit() {
        binding.apply {
            adapterFeedback.subjectFeedbackItemPosition = { item, selectedList ->
                listChooseData.clear()
                listChooseData.addAll(selectedList.map {
                    it.title
                })
                cardSubmit.isGone = listChooseData.isEmpty()
            }
            cardSubmit.clicks {
                listChooseData.map {
                    dataFeedback = dataFeedback + it + "\n"
                }
                sendEmailViaGmail(
                    this@FeedbackActivity,
                    "Voidmaink39c@gmail.com",
                    "Feedback QRCODE_2025",
                    dataFeedback
                )
            }
            backButton.clicks {
                finish()
            }
        }

    }

    private fun initSetData() {
        val feedBacks: MutableList<FeedBack> = mutableListOf()
        feedBacks.add(FeedBack(getString(R.string.scanningText)))
        feedBacks.add(FeedBack(getString(R.string.tooManyAdsText)))
        feedBacks.add(FeedBack(getString(R.string.needMoreText)))
        feedBacks.add(FeedBack(getString(R.string.othersText)))
        adapterFeedback.data = feedBacks
        binding.rvChooseType.adapter = adapterFeedback
    }
}