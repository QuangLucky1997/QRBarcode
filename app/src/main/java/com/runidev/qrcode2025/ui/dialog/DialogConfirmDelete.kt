package com.runidev.qrcode2025.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.runidev.qrcode2025.R
import com.runidev.qrcode2025.base.BaseDialog
import com.runidev.qrcode2025.databinding.DialogConfirmDialogBinding
import com.runidev.qrcode2025.util.ext.clicks
import javax.inject.Inject

class DialogConfirmDelete @Inject constructor(
) : BaseDialog<DialogConfirmDialogBinding>() {
    var actionDelete: () -> Unit = {}
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> DialogConfirmDialogBinding
        get() = DialogConfirmDialogBinding::inflate

    override fun onViewCreated() {
        initEvent()
    }

    private fun initEvent() {
        binding.apply {
            cancel.clicks {
                dismiss()
            }
            delete.clicks {
                actionDelete()
                dismiss()
            }
        }
    }
}