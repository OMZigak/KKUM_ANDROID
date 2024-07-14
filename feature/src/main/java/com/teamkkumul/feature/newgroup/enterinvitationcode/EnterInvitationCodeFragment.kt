package com.teamkkumul.feature.newgroup.enterinvitationcode

import androidx.core.widget.doAfterTextChanged
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentEnterInvitationCodeBinding
import com.teamkkumul.feature.utils.Debouncer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterInvitationCodeFragment :
    BindingFragment<FragmentEnterInvitationCodeBinding>(R.layout.fragment_enter_invitation_code) {

    private val enterInvitationCodeDebouncer = Debouncer<String>()
    private var currentText: String = ""

    override fun initView() {
        initBlockEnterKey()
        setupInvitationCode()
        setupNextButton()
    }

    private fun initBlockEnterKey() = with(binding.etEnterInvitationCode) {
        setOnEditorActionListener { _, actionId, event ->
            (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER))
        }
    }

    private fun setupInvitationCode() {
        binding.etEnterInvitationCode.doAfterTextChanged { editable ->
            val input = editable?.toString().orEmpty()
            enterInvitationCodeDebouncer.setDelay(input, 200L) { code ->
                updateButtonState(code.length == 6)
            }
        }
    }

    private fun setupNextButton() {
        binding.btnNext.setOnClickListener {
            val input = binding.etEnterInvitationCode.text.toString()
            validInput(input)
        }
    }

    private fun validInput(input: String) { // 서버 통신 patch 로직 추가 필요
        val isValid = input.length == 6
        if (isValid) {
            currentText = input
            setErrorState(null)
        } else {
            setColor(R.color.red)
            setErrorState(getString(R.string.set_enter_invitation_code_error_message))
        }
    }

    private fun setErrorState(errorMessage: String?) {
        with(binding) {
            tilEnterInvitationCode.error = errorMessage
            tilEnterInvitationCode.isErrorEnabled = errorMessage != null
        }
    }

    private fun setColor(colorResId: Int) {
        val color = colorOf(colorResId)
        with(binding) {
            tilEnterInvitationCode.boxStrokeColor = color
        }
    }

    private fun updateButtonState(isValid: Boolean) {
        binding.btnNext.isEnabled = isValid
    }
}
