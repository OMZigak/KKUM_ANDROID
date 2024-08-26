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
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentDialogDeleteBinding
import com.teamkkumul.feature.utils.DeleteDialogType
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
        val promiseId = args.promiseId
        val meetingId = args.meetingId

        setUpDialog(dialogType)
        initDeleteBtnClickListener(dialogType, promiseId, meetingId)
        initCancelBtnClickListener()
        observeDeleteMyGroupState()
        observeLeaveMeetUpState()
        observeDeleteMeetUpState()
    }

    private fun setUpDialog(dialogType: DeleteDialogType) {
        with(binding) {
            ivDialogLeave.load(dialogType.imageResId)
            tvLeaveQuestion.text = getString(dialogType.question)
            tvLeaveQuestionDescription.text = getString(dialogType.questionDescription)
            tvBtnLeave.text = getString(dialogType.btnText)
        }
        if (dialogType == DeleteDialogType.PROMISE_DELETE_DIALOG) {
            binding.tvLeaveQuestionDescription.setTextColor(colorOf(R.color.red))
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
            }

            DeleteDialogType.PROMISE_LEAVE_DIALOG -> {
                viewModel.leaveMeetUp(promiseId)
            }

            DeleteDialogType.PROMISE_DELETE_DIALOG -> {
                viewModel.deleteMeetUp(promiseId)
            }
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

    private fun observeLeaveMeetUpState() {
        viewModel.leaveMeetUpState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    findNavController().popBackStack(R.id.fragment_my_group_detail, false)
                    dismiss()
                }

                is UiState.Failure -> requireContext().toast(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun observeDeleteMeetUpState() {
        viewModel.deleteMeetUpState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    findNavController().popBackStack(R.id.fragment_my_group_detail, false)
                    dismiss()
                }

                is UiState.Failure -> requireContext().toast(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
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
