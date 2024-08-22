package com.teamkkumul.feature.mygroup.dialog

import com.teamkkumul.core.ui.base.BindingDialogFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogDeleteBinding
import com.teamkkumul.feature.utils.DeleteDialogType

class MyGroupLeaveDialogFragment :
    BindingDialogFragment<FragmentDialogDeleteBinding>(R.layout.fragment_dialog_delete) {

    private var _binding: FragmentDialogDeleteBinding? = null

    //    private lateinit var type: DeleteDialogType
    override fun initView() {
        setUpDialog(DeleteDialogType.MY_GROUP_LEAVE_DIALOG)
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
