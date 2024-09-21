package com.teamkkumul.feature.newgroup.addnewgroup

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingDialogFragment
import com.teamkkumul.core.ui.util.context.dialogFragmentResize
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogInvitationCodeBinding
import com.teamkkumul.feature.utils.KeyStorage.ADD_NEW_GROUP_FRAGMENT
import com.teamkkumul.feature.utils.KeyStorage.CODE
import com.teamkkumul.feature.utils.KeyStorage.MEETING_ID
import com.teamkkumul.feature.utils.KeyStorage.MY_GROUP_DETAIL_FRAGMENT
import com.teamkkumul.feature.utils.KeyStorage.SOURCE_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DialogInvitationCodeFragment :
    BindingDialogFragment<FragmentDialogInvitationCodeBinding>(R.layout.fragment_dialog_invitation_code) {

    private val invitationCode: String by lazy {
        arguments?.getString(CODE) ?: ""
    }

    private val meetingId: Int by lazy {
        requireArguments().getInt(MEETING_ID)
    }

    private val sourceFragment: String by lazy {
        arguments?.getString(SOURCE_FRAGMENT) ?: ""
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
        binding.tvBtnCopy.setOnClickListener {
            copyToClipboard(invitationCode)
            when (sourceFragment) {
                ADD_NEW_GROUP_FRAGMENT -> {
                    if (invitationCode.isNotEmpty()) {
                        findNavController().navigate(
                            R.id.action_dialog_to_completed,
                            bundleOf(MEETING_ID to meetingId)
                        )
                    } else {
                        findNavController().navigate(R.id.action_dialog_to_my_group_detail)
                    }
                    dismiss()
                }
                MY_GROUP_DETAIL_FRAGMENT -> {
                    dismiss()
                }
            }
        }
        binding.tvBtnInviteLater.setOnClickListener {
            when (sourceFragment) {
                ADD_NEW_GROUP_FRAGMENT -> {
                    if (invitationCode.isNotEmpty()) {
                        findNavController().navigate(
                            R.id.action_dialog_to_completed,
                            bundleOf(MEETING_ID to meetingId)
                        )
                    } else {
                        findNavController().navigate(R.id.action_dialog_to_my_group_detail)
                    }
                    dismiss()
                }
                MY_GROUP_DETAIL_FRAGMENT -> {
                    dismiss()
                }
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("invitation_code", text)
        clipboard.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            toast(getString(R.string.toast_clipboard))
        }
    }
}
