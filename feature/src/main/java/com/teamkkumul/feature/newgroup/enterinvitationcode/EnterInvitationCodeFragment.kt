package com.teamkkumul.feature.newgroup.enterinvitationcode

import android.text.Editable
import android.text.TextWatcher
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentEnterInvitationCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterInvitationCodeFragment :
    BindingFragment<FragmentEnterInvitationCodeBinding>(R.layout.fragment_enter_invitation_code) {

    private var currentText: String = ""

    override fun initView() {
        blockEnterKey()

        binding.etEnterInvitationCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                updateButtonState(input.length == 6)
            }
        })

        binding.btnNext.setOnClickListener {
            val input = binding.etEnterInvitationCode.text.toString()
            validInput(input)
        }
    }

    private fun blockEnterKey() = with(binding.etEnterInvitationCode) {
        setOnEditorActionListener { _, actionId, event ->
            (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER))
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
