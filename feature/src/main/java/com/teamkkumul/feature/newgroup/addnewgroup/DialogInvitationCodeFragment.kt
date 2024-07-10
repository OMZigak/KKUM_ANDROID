package com.teamkkumul.feature.newgroup.addnewgroup

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingDialogFragment
import com.teamkkumul.core.ui.util.context.dialogFragmentResize
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogInvitationCodeBinding

class DialogInvitationCodeFragment :
    BindingDialogFragment<FragmentDialogInvitationCodeBinding>(R.layout.fragment_dialog_invitation_code) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun initView() {
        binding.ivBtnCopy.setOnClickListener {
            copyToClipboard(binding.tvInvitationCode.text.toString())
            findNavController().navigate(R.id.action_fragment_add_new_group_to_fragment_add_my_group_complete)
            dismiss()
        }
        binding.ivBtnInviteLater.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_add_new_group_to_fragment_add_my_group_complete)
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }

    private fun copyToClipboard(text: String) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("invitation_code", text)
        clipboard.setPrimaryClip(clip)
    }
}
