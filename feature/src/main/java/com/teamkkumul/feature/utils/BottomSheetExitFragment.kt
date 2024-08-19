package com.teamkkumul.feature.utils

import com.teamkkumul.core.ui.base.BindingBottomSheetFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentExitBottomSheetBinding
import com.teamkkumul.feature.utils.KeyStorage.GROUP_NAME
import com.teamkkumul.feature.utils.KeyStorage.MEETING_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetExitFragment :
    BindingBottomSheetFragment<FragmentExitBottomSheetBinding>(R.layout.fragment_exit_bottom_sheet) {
    private val currentId: Int by lazy { arguments?.getInt(MEETING_ID, -1) ?: -1 }
    private val groupName: String by lazy { arguments?.getString(GROUP_NAME).orEmpty() }
    override fun initView() {
        myGroupPlusButton()
    }

    private fun myGroupPlusButton() {
        binding.tvGroupName.text = groupName
        binding.tvCancleBackground.setOnClickListener {
            dismiss()
        }
    }
}
