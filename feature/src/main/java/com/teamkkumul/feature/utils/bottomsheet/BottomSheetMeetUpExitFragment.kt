package com.teamkkumul.feature.utils.bottomsheet

import com.teamkkumul.core.ui.base.BindingBottomSheetFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentExitMeetUpBottomSheetBinding
import com.teamkkumul.feature.utils.KeyStorage

class BottomSheetMeetUpExitFragment :
    BindingBottomSheetFragment<FragmentExitMeetUpBottomSheetBinding>(R.layout.fragment_exit_meet_up_bottom_sheet) {
    private val currentId: Int by lazy { arguments?.getInt(KeyStorage.PROMISE_ID, -1) ?: -1 }
    private val meetUpName: String by lazy {
        arguments?.getString(KeyStorage.MEET_UP_NAME).orEmpty()
    }

    override fun initView() {
        setMeetUpName()
    }

    private fun setMeetUpName() {
        binding.tvMeetUpName.text = meetUpName
    }
}
