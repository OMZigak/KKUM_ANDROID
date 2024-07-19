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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogInvitationCodeFragment :
    BindingDialogFragment<FragmentDialogInvitationCodeBinding>(R.layout.fragment_dialog_invitation_code) {

    private val invitationCode: String by lazy {
        requireArguments().getString("code") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun initView() {
        getInvitationCode()
        setupDialogBtn()
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }

    private fun getInvitationCode() {
        if (invitationCode.isNotEmpty()) {
            binding.tvInvitationCode.text = invitationCode
        }
    }

    private fun setupDialogBtn() {
        binding.ivBtnCopy.setOnClickListener {
            copyToClipboard(invitationCode)
            if (invitationCode.isNotEmpty()) {
                findNavController().navigate(R.id.action_dialog_to_completed)
            } else {
                findNavController().navigate(R.id.action_dialog_to_my_group_detail)
            }
            dismiss()
        }
        binding.ivBtnInviteLater.setOnClickListener {
            dismiss()
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("invitation_code", text)
        clipboard.setPrimaryClip(clip)
    }

    companion object {
        private const val SOURCE_FRAGMENT = "sourceFragment"

        @JvmStatic
        fun newInstance(invitationCode: String) =
            DialogInvitationCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(SOURCE_FRAGMENT, invitationCode)
                }
            }
    }
}
