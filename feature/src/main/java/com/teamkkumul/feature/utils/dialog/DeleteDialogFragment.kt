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
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.util.intent.navigateTo
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.core.ui.view.setVisible
import com.teamkkumul.feature.R
import com.teamkkumul.feature.auth.LoginActivity
import com.teamkkumul.feature.databinding.FragmentDialogDeleteBinding
import com.teamkkumul.feature.utils.extension.isImageVisible
import com.teamkkumul.feature.utils.extension.shouldChangeDescriptionColor
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
        observeLeaveMeetUpState()
        observeDeleteMeetUpState()
        observeWithdrawState()
        observeLogoutState()
    }

    private fun setUpDialog(dialogType: DeleteDialogType) = with(binding) {
        // 이미지 visibilty 및 로드
        val imgVisibility = dialogType.isImageVisible()
        ivDialogLeave.setVisible(imgVisibility)
        if (imgVisibility) ivDialogLeave.load(dialogType.imageResId)

        // 텍스트 설정
        tvLeaveQuestion.text = getString(dialogType.question)
        tvLeaveQuestionDescription.text = getString(dialogType.questionDescription)
        tvBtnLeave.text = getString(dialogType.btnLeaveText)

        // 설명 텍스트 색상 변경
        if (dialogType.shouldChangeDescriptionColor()) {
            tvLeaveQuestionDescription.setTextColor(colorOf(R.color.red))
        }
    }

    private fun initDeleteBtnClickListener(onDeleteAction: () -> Unit) {
        binding.tvBtnLeave.setOnClickListener {
            onDeleteAction()
        }
    }

    private fun handleDeleteAction(args: DeleteDialogFragmentArgs) {
        when (args.dialogType) {
            DeleteDialogType.MY_GROUP_LEAVE_DIALOG -> viewModel.deleteMyGroup(args.meetingId)

            DeleteDialogType.PROMISE_LEAVE_DIALOG -> viewModel.leaveMeetUp(args.promiseId)

            DeleteDialogType.PROMISE_DELETE_DIALOG -> viewModel.deleteMeetUp(args.promiseId)

            DeleteDialogType.Logout -> viewModel.postLogout()

            DeleteDialogType.Withdrawal -> viewModel.deleteWithdrawal()
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

    private fun observeLogoutState() {
        viewModel.logoutState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> navigateTo<LoginActivity>(requireContext())
                is UiState.Failure -> toast(it.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun observeWithdrawState() {
        viewModel.withdrawState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> navigateTo<LoginActivity>(requireContext())
                is UiState.Failure -> toast(it.errorMessage)
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
