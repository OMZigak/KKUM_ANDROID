package com.teamkkumul.feature.utils.bottomsheet

import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingBottomSheetFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentExitMeetUpBottomSheetBinding
import com.teamkkumul.feature.utils.DeleteDialogType
import com.teamkkumul.feature.utils.KeyStorage.MEET_UP_NAME
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID

class BottomSheetMeetUpExitFragment :
    BindingBottomSheetFragment<FragmentExitMeetUpBottomSheetBinding>(R.layout.fragment_exit_meet_up_bottom_sheet) {
    private val currentId: Int by lazy { arguments?.getInt(PROMISE_ID, -1) ?: -1 }
    private val meetUpName: String by lazy { arguments?.getString(MEET_UP_NAME).orEmpty() }

    override fun initView() {
        setMeetUpName()
    }

    private fun setMeetUpName() {
        binding.tvMeetUpName.text = meetUpName
        binding.tvExitMeetUpBackground.setOnClickListener {
            val action =
                BottomSheetMeetUpExitFragmentDirections.actionBottomSheetMeetUpExitFragmentToDeleteDialogFragment(
                    dialogType = DeleteDialogType.PROMISE_LEAVE_DIALOG,
                    promiseId = currentId,
                )
            findNavController().navigate(action)
        }
        binding.tvDeleteMeetUpBackground.setOnClickListener {
            val action =
                BottomSheetMeetUpExitFragmentDirections.actionBottomSheetMeetUpExitFragmentToDeleteDialogFragment(
                    dialogType = DeleteDialogType.PROMISE_DELETE_DIALOG,
                    promiseId = currentId,
                )
            findNavController().navigate(action)
        }
    }
}
