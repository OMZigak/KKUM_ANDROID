package com.teamkkumul.feature.newgroup.addnewgroup

import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentAddNewGroupBinding
import com.teamkkumul.feature.utils.Debouncer
import com.teamkkumul.feature.utils.KeyStorage.ADD_NEW_GROUP_FRAGMENT
import com.teamkkumul.feature.utils.KeyStorage.CODE
import com.teamkkumul.feature.utils.KeyStorage.MEETING_ID
import com.teamkkumul.feature.utils.KeyStorage.SOURCE_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddNewGroupFragment :
    BindingFragment<FragmentAddNewGroupBinding>(R.layout.fragment_add_new_group) {
    private val viewModel: AddNewGroupViewModel by viewModels()
    private val groupNameDebouncer = Debouncer<String>()
    private var currentText: String = ""

    override fun initView() {
        setName()
        binding.btnMakeNewGroup.setOnClickListener {
            val name = binding.etSetGroupName.text.toString()
            viewModel.addNewGroup(name)
        }

        viewModel.meetingsState.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    showInvitationDialog(it.data.meetingId, it.data.invitationCode)
                }

                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun setName() = with(binding.etSetGroupName) {
        doAfterTextChanged {
            groupNameDebouncer.setDelay(text.toString(), SET_NAME_DEBOUNCE_DELAY, ::validInput)
        }
        setOnEditorActionListener { _, actionId, event ->
            (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER))
        }
    }

    private fun validInput(input: String) {
        val isValid = input.length <= GROUP_NAME_MAX_LENGTH && input.matches(nameRegex)
        if (isValid) {
            currentText = input
            setColor(R.color.main_color)
            setInputTextColor(R.color.black0)
            setErrorState(null)
        } else {
            setColor(R.color.red)
            setInputTextColor(R.color.red)
            setErrorState(getString(R.string.set_group_name_error_message))
        }
        updateCounter(input.length)
        updateButtonState(isValid)
    }

    private fun setErrorState(errorMessage: String?) {
        with(binding) {
            tilSetGroupName.error = errorMessage
            tilSetGroupName.isErrorEnabled = errorMessage != null
        }
    }

    private fun setColor(colorResId: Int) {
        val color = colorOf(colorResId)
        with(binding) {
            tvCounter.setTextColor(color)
            tilSetGroupName.boxStrokeColor = color
        }
    }

    private fun setInputTextColor(colorResId: Int) {
        binding.etSetGroupName.setTextColor(colorOf(colorResId))
    }

    private fun updateCounter(length: Int) {
        binding.tvCounter.text = "$length/10"
    }

    private fun updateButtonState(isValid: Boolean) {
        binding.btnMakeNewGroup.isEnabled = isValid
    }

    private fun showInvitationDialog(meetingId: Int, invitationCode: String) {
        findNavController().navigate(
            R.id.fragment_dialog_invitation_code,
            bundleOf(MEETING_ID to meetingId, CODE to invitationCode, SOURCE_FRAGMENT to ADD_NEW_GROUP_FRAGMENT),
        )
    }

    companion object {
        private const val NAME_PATTERN = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣\\s]{1,10}$"
        private val nameRegex = Regex(NAME_PATTERN)
        private const val SET_NAME_DEBOUNCE_DELAY = 300L
        private const val GROUP_NAME_MAX_LENGTH = 10
    }
}
