package com.teamkkumul.feature.newgroup.enterinvitationcode

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentEnterInvitationCodeBinding
import com.teamkkumul.feature.utils.Debouncer
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.KeyStorage.MEETING_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class EnterInvitationCodeFragment :
    BindingFragment<FragmentEnterInvitationCodeBinding>(R.layout.fragment_enter_invitation_code) {

    private val viewModel by viewModels<InvitationCodeViewModel>()
    private val enterInvitationCodeDebouncer = Debouncer<String>()

    override fun initView() {
        initInvitationCode()
        initBlockEnterKey()
        setupInvitationCode()
        setupNextButton()
        observeViewModel()
    }

    private fun initBlockEnterKey() = with(binding.etEnterInvitationCode) {
        setOnEditorActionListener { _, actionId, event ->
            (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER))
        }
    }

    private fun setupInvitationCode() {
        binding.etEnterInvitationCode.doAfterTextChanged { editable ->
            val input = editable?.toString().orEmpty()
            enterInvitationCodeDebouncer.setDelay(input, 200L) { invitationCode ->
                updateButtonState(invitationCode.length == 6)
            }
        }
    }

    private fun setupNextButton() {
        binding.btnNext.setOnClickListener {
            val input = binding.etEnterInvitationCode.text.toString()
            viewModel.validInput(input)
        }
    }

    private fun observeViewModel() {
        val id = arguments?.getInt(KeyStorage.MEETING_ID) ?: -1
//            viewModel.meetingsState.collect { state ->
//                when (state) {
//                    is UiState.Success -> {
//                        binding.ivInvitationCodeCheck.visibility = View.VISIBLE
//                        delay(500L)
//                        findNavController().navigate(
//                            R.id.action_fragment_enter_invitation_code_to_fragment_my_group_detail,
//                            bundleOf(MEETING_ID to id),
//                        )
//                        Timber.tag("첫번째보냄").d(id.toString())
//                    }
//                    is UiState.Failure -> {
//                        setErrorState(getString(R.string.set_enter_invitation_code_error_message))
//                    }
//                    else -> Unit
//                }
//            }
        viewModel.meetingsState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        binding.ivInvitationCodeCheck.visibility = View.VISIBLE
                        delay(500L)
                        val meetingId = state.data
                        findNavController().navigate(
                            R.id.action_fragment_enter_invitation_code_to_fragment_my_group_detail,
                            bundleOf(MEETING_ID to meetingId),
                        )
                        Timber.tag("첫번째보냄").d(meetingId.toString())
                    }
                    is UiState.Failure -> {
                        setErrorState(getString(R.string.set_enter_invitation_code_error_message))
                    }
                    else -> Unit
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initInvitationCode() {
        binding.ivInvitationCodeCheck.visibility = View.GONE
    }

    private fun setErrorState(errorMessage: String?) {
        with(binding) {
            tilEnterInvitationCode.error = errorMessage
            tilEnterInvitationCode.isErrorEnabled = errorMessage != null
            tilEnterInvitationCode.boxStrokeColor = colorOf(R.color.red)
        }
    }

    private fun updateButtonState(isValid: Boolean) {
        binding.btnNext.isEnabled = isValid
    }
}
