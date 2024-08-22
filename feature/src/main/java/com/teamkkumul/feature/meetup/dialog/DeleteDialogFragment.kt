package com.teamkkumul.feature.meetup.dialog

import androidx.navigation.fragment.navArgs
import coil.load
import com.teamkkumul.core.ui.base.BindingDialogFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogDeleteBinding
import com.teamkkumul.feature.utils.DeleteDialogType
import com.teamkkumul.feature.utils.KeyStorage.DIALOG_TYPE

class DeleteDialogFragment :
    BindingDialogFragment<FragmentDialogDeleteBinding>(R.layout.fragment_dialog_delete) {

    private var _binding: FragmentDialogDeleteBinding? = null

    private lateinit var dialogType: DeleteDialogType
    override fun initView() {
//        val args: DeleteDialogFragmentArgs by navArgs()
//        val typeString = args.dialogType
//        binding.ivDialogLeave.load(typeString.imageResId)

//        val dialogType2 = DeleteDialogType.valueOf(typeString)

        val dialogTypeString = requireArguments().getString(DIALOG_TYPE)
        if (dialogTypeString != null) {
            dialogType = DeleteDialogType.valueOf(dialogTypeString)
        }
        setUpDialog()
    }

    private fun setUpDialog() {
        with(binding) {
            ivDialogLeave.load(dialogType.imageResId)
            tvLeaveQuestion.text = dialogType.question
            tvLeaveQuestionDescription.text = dialogType.questionDescription
            tvBtnLeave.text = dialogType.btnText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
