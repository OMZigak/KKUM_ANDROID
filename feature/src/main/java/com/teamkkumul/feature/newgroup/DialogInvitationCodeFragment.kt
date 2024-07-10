package com.teamkkumul.feature.newgroup

import android.os.Bundle
import android.view.WindowManager
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
        // Initialize your views here
    }
}
