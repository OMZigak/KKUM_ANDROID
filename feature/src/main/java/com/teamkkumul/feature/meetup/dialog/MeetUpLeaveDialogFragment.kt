package com.teamkkumul.feature.meetup.dialog

import com.teamkkumul.core.ui.base.BindingDialogFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogDeleteBinding
import com.teamkkumul.feature.utils.DeleteDialogType

class MeetUpLeaveDialogFragment :
    BindingDialogFragment<FragmentDialogDeleteBinding>(R.layout.fragment_dialog_delete) {

    private var _binding: FragmentDialogDeleteBinding? = null

    override fun initView() {
        setUpDialog(DeleteDialogType.MEET_UP_LEAVE_DIALOG)
    }

    private fun setUpDialog(dialogType: DeleteDialogType) {
        with(binding) {
            ivDialogLeave.setImageResource(dialogType.imageResId)
            tvLeaveQuestion.text = dialogType.question
            tvLeaveQuestionDescription.text = dialogType.questionDescription
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
