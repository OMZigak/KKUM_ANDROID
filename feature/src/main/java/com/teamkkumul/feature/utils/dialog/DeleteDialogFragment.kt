package com.teamkkumul.feature.utils.dialog

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.teamkkumul.core.ui.base.BindingDialogFragment
import com.teamkkumul.core.ui.util.context.dialogFragmentResize
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogDeleteBinding
import com.teamkkumul.feature.utils.DeleteDialogType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteDialogFragment :
    BindingDialogFragment<FragmentDialogDeleteBinding>(R.layout.fragment_dialog_delete) {
    private val viewModel by viewModels<DeleteDialogViewModel>()

    override fun initView() {
        val args: DeleteDialogFragmentArgs by navArgs()
        val dialogType = args.dialogType
        val promiseId = args.promiseId
        val meetingId = args.meetingId

        setUpDialog(dialogType)
        initDeleteBtnClickListener(dialogType, promiseId, meetingId)
        initCancelBtnClickListener()
    }

    private fun setUpDialog(dialogType: DeleteDialogType) {
        with(binding) {
            ivDialogLeave.load(dialogType.imageResId)
            tvLeaveQuestion.text = getString(dialogType.question)
            tvLeaveQuestionDescription.text = getString(dialogType.questionDescription)
            tvBtnLeave.text = getString(dialogType.btnText)
        }
    }

    private fun initDeleteBtnClickListener(
        dialogType: DeleteDialogType,
        promiseId: Int,
        meetingId: Int,
    ) {
        binding.tvBtnLeave.setOnClickListener {
            handleDeleteAction(dialogType, promiseId, meetingId)
        }
    }

    private fun handleDeleteAction(dialogType: DeleteDialogType, promiseId: Int, meetingId: Int) {
        when (dialogType) {
            DeleteDialogType.MY_GROUP_LEAVE_DIALOG -> {
                viewModel.deleteMyGroup(meetingId)
                findNavController().popBackStack(R.id.fragment_my_group, false)
                // viewModel.deleteGroup(args.meetingId)
                // findNavController().navigate() 및 stack 제거 처리
            }

            DeleteDialogType.PROMISE_LEAVE_DIALOG -> {
                // viewModel.deleteMeetUp(args.promiseId)
                // findNavController().navigate("key" to meetingId) 및 stack 제거 처리
            }

            DeleteDialogType.PROMISE_DELETE_DIALOG -> {
                // viewModel.deleteMeetUp(args.promiseId)
                // findNavController().navigate("key" to meetingId) 및 stack 제거 처리
                binding.tvLeaveQuestionDescription.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red,
                    ),
                )
            }
        }
        dismiss()
    }

    private fun initCancelBtnClickListener() {
        binding.tvBtnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 34.0f)
    }
}
