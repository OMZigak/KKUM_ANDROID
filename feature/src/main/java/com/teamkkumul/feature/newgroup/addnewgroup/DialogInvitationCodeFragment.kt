package com.teamkkumul.feature.newgroup.addnewgroup

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingDialogFragment
import com.teamkkumul.core.ui.util.bundle.getSafeParcelable
import com.teamkkumul.core.ui.util.context.dialogFragmentResize
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogInvitationCodeBinding
import com.teamkkumul.feature.utils.KeyStorage.ADD_NEW_GROUP_MODEL
import com.teamkkumul.feature.utils.KeyStorage.MEETING_ID
import com.teamkkumul.model.AddNewGroupModel
import com.teamkkumul.model.type.ScreenType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogInvitationCodeFragment :
    BindingDialogFragment<FragmentDialogInvitationCodeBinding>(R.layout.fragment_dialog_invitation_code) {
    private val addNewGroupModel: AddNewGroupModel? by lazy {
        arguments?.getSafeParcelable(ADD_NEW_GROUP_MODEL)
    }

    private val invitationCode: String by lazy {
        addNewGroupModel?.invitationCode.orEmpty()
    }

    private val meetingId: Int by lazy {
        addNewGroupModel?.meetingId ?: -1
    }

    private val sourceFragment: ScreenType by lazy {
        addNewGroupModel?.screenType ?: ScreenType.MY_GROUP_DETAIL
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
        binding.tvInvitationCode.text = invitationCode
    }

    private fun setupDialogBtn() {
        binding.tvBtnCopy.setOnClickListener {
            when (sourceFragment) {
                ScreenType.ADD_NEW_GROUP -> {
                    if (invitationCode.isNotEmpty()) {
                        copyToClipboard(invitationCode)
                        navigateToAddMyGroupComplete()
                    } else {
                        findNavController().navigate(R.id.action_dialog_to_my_group_detail)
                    }
                    dismiss()
                }

                ScreenType.MY_GROUP_DETAIL -> {
                    copyToClipboard(invitationCode)
                    dismiss()
                }
            }
        }

        binding.tvBtnInviteLater.setOnClickListener {
            when (sourceFragment) {
                ScreenType.ADD_NEW_GROUP -> {
                    if (invitationCode.isNotEmpty()) {
                        navigateToAddMyGroupComplete()
                    } else {
                        findNavController().navigate(R.id.action_dialog_to_my_group_detail)
                    }
                    dismiss()
                }

                ScreenType.MY_GROUP_DETAIL -> dismiss()
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

    private fun navigateToAddMyGroupComplete() {
        findNavController().navigate(
            R.id.action_dialog_to_completed,
            bundleOf(MEETING_ID to meetingId),
        )
    }
}
