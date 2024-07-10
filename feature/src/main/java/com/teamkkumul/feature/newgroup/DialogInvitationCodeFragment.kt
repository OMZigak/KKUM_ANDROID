package com.teamkkumul.feature.newgroup

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingDialogFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogInvitationCodeBinding

class DialogInvitationCodeFragment :
    BindingDialogFragment<FragmentDialogInvitationCodeBinding>(R.layout.fragment_dialog_invitation_code) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun initView() {
        binding.ivBtnCopy.setOnClickListener {
            copyToClipboard(binding.tvInvitationCode.text.toString())
            findNavController().navigate(R.id.action_fragment_dialog_invitation_code_to_fragment_add_my_group_complete)
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("invitation_code", text)
        clipboard.setPrimaryClip(clip)
    }
}
