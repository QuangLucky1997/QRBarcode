package com.runidev.qrcode2025.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.runidev.qrcode2025.databinding.CustomFeedbackBinding
import com.runidev.qrcode2025.enumData.FeedBack
import com.runidev.qrcode2025.util.ext.clicks
import javax.inject.Inject

class FeedbackAdapter @Inject constructor() :
    com.runidev.qrcode2025.base.BaseAdapter<FeedBack, CustomFeedbackBinding>() {

    var subjectFeedbackItemPosition: ((FeedBack, ArrayList<FeedBack>) -> Unit)? = null
    private val selectedList = arrayListOf<FeedBack>()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomFeedbackBinding
        get() = CustomFeedbackBinding::inflate

    @SuppressLint("ResourceAsColor")
    override fun bindItem(
        item: FeedBack,
        binding: CustomFeedbackBinding,
        position: Int
    ) {
        binding.apply {
            textItemFeedback.text = item.title
            backgroundItemFeedback.setBackgroundResource(if (item.isChoose) com.runidev.qrcode2025.R.color.darkgreen else com.runidev.qrcode2025.R.color.white)
            textItemFeedback.setTextColor(if (item.isChoose) com.runidev.qrcode2025.R.color.white else com.runidev.qrcode2025.R.color.black)
            cardType.clicks {
                item.isChoose = !item.isChoose
                if (item.isChoose) {
                    selectedList.add(item)
                } else {
                    selectedList.remove(item)
                }
                notifyItemChanged(position)
                subjectFeedbackItemPosition?.invoke(item, ArrayList(selectedList))


            }

        }
    }


}
