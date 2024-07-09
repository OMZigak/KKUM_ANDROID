package com.teamkkumul.feature.newgroup

import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentAddNewGroupBinding
import com.teamkkumul.feature.utils.Debouncer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewGroupFragment : BindingFragment<FragmentAddNewGroupBinding>(R.layout.fragment_add_new_group) {
    private val groupNameViewModel: GroupNameViewModel by activityViewModels()
    private val groupNameDebouncer = Debouncer<String>()
    private var currentText: String = ""

    override fun initView() {
        setName()
        binding.btnNext.setOnClickListener {
            groupNameViewModel.getGroupName(binding.etSetGroupName.text.toString())
            findNavController().navigate(R.id.action_fragment_set_name_to_fragment_set_profile)
        }
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
            setErrorState(null)
        } else {
            setColor(R.color.red)
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
            etSetGroupName.setTextColor(color)
            tilSetGroupName.boxStrokeColor = color
        }
    }

    private fun updateCounter(length: Int) {
        binding.tvCounter.text = "${length.coerceAtMost(10)}/10"
    }

    private fun updateButtonState(isValid: Boolean) {
        binding.btnNext.isEnabled = isValid
    }

    companion object {
        private const val NAME_PATTERN = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣\\s]{1,10}$"
        private val nameRegex = Regex(NAME_PATTERN)
        private const val SET_NAME_DEBOUNCE_DELAY = 300L
        private const val GROUP_NAME_MAX_LENGTH = 10
    }
}
