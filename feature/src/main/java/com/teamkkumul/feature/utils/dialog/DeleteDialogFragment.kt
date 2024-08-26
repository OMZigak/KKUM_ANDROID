package com.teamkkumul.feature.utils.dialog

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.teamkkumul.core.ui.base.BindingDialogFragment
import com.teamkkumul.core.ui.util.context.dialogFragmentResize
import com.teamkkumul.core.ui.util.context.toast
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.core.ui.view.setVisible
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogDeleteBinding
import com.teamkkumul.feature.utils.type.DeleteDialogType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DeleteDialogFragment :
    BindingDialogFragment<FragmentDialogDeleteBinding>(R.layout.fragment_dialog_delete) {
    private val viewModel by viewModels<DeleteDialogViewModel>()

    override fun initView() {
        val args: DeleteDialogFragmentArgs by navArgs()
        val dialogType = args.dialogType

        setUpDialog(dialogType)
        initDeleteBtnClickListener { handleDeleteAction(args) }
        initCancelBtnClickListener()
        observeDeleteMyGroupState()
    }

    private fun setUpDialog(dialogType: DeleteDialogType) = with(binding) {
        val isImageVisible = checkImageVisibility(dialogType)
        tvBtnCancel.setVisible(isImageVisible)
        if (isImageVisible) ivDialogLeave.load(dialogType.imageResId)
        tvLeaveQuestion.text = getString(dialogType.question)
        tvLeaveQuestionDescription.text = getString(dialogType.questionDescription)
        tvBtnLeave.text = getString(dialogType.btnLeaveText)
    }

    private fun checkImageVisibility(dialogType: DeleteDialogType) = when (dialogType) {
        DeleteDialogType.Logout, DeleteDialogType.Withdrawal -> false
        else -> true
    }

    // 여기에서 람다를 파라미터로 받음
    private fun initDeleteBtnClickListener(onDeleteAction: () -> Unit) {
        binding.tvBtnLeave.setOnClickListener {
            onDeleteAction()
        }
    }

    private fun handleDeleteAction(args: DeleteDialogFragmentArgs) = with(binding) {
        when (args.dialogType) {
            DeleteDialogType.MY_GROUP_LEAVE_DIALOG -> {
                viewModel.deleteMyGroup(args.meetingId)
            }

            DeleteDialogType.PROMISE_LEAVE_DIALOG -> {
                // viewModel.deleteMeetUp(args.promiseId)
                // findNavController().navigate("key" to meetingId) 및 stack 제거 처리
            }

            DeleteDialogType.PROMISE_DELETE_DIALOG -> {
                tvLeaveQuestionDescription.setTextColor(colorOf(R.color.red))
            }

            DeleteDialogType.Logout -> {}

            DeleteDialogType.Withdrawal -> {}
        }
    }

    private fun observeDeleteMyGroupState() {
        viewModel.deleteMyGroupState.flowWithLifecycle(viewLifeCycle).onEach { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    findNavController().popBackStack(R.id.fragment_my_group, false)
                    dismiss()
                }

                is UiState.Failure -> requireContext().toast(uiState.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initCancelBtnClickListener() {
        binding.tvBtnCancel.setOnClickListener { dismiss() }
    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogFragmentResize(this, 27f)
    }
}
