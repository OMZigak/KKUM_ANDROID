package com.teamkkumul.feature.signup

import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentSetNameBinding
import com.teamkkumul.feature.utils.Debouncer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetNameFragment : BindingFragment<FragmentSetNameBinding>(R.layout.fragment_set_name) {

    private val setNameViewModel: SetNameViewModel by activityViewModels()
    private val setNameDebouncer = Debouncer<String>()
    private var currentText: String = ""

    override fun initView() {
        setName()
        binding.btnNext.setOnClickListener {
            setNameViewModel.getInputName(binding.etSetName.text.toString())
            findNavController().navigate(R.id.action_fragment_set_name_to_fragment_set_profile)
        }
    }

    private fun setName() = with(binding.etSetName) {
        doAfterTextChanged {
            setNameDebouncer.setDelay(text.toString(), 300L, ::validInput)
        }
    }

    private fun validInput(input: String) {
        val isValid = input.length <= 5 && input.matches(nameRegex)
        if (isValid) {
            currentText = input
            setColor(R.color.main_color)
            setErrorState(null)
        } else {
            setColor(R.color.red)
            setErrorState("한글, 영문, 숫자만을 사용해 총 5자 이내로 입력해주세요.")
        }
        updateCounter(input.length)
        updateButtonState(isValid)
    }

    private fun setErrorState(errorMessage: String?) {
        with(binding) {
            tilSetName.error = errorMessage
            tilSetName.isErrorEnabled = errorMessage != null
        }
    }

    private fun setColor(colorResId: Int) {
        val color = ContextCompat.getColor(requireContext(), colorResId)
        with(binding) {
            tvCounter.setTextColor(color)
            etSetName.setTextColor(color)
            tilSetName.boxStrokeColor = color
        }
    }

    private fun updateCounter(length: Int) {
        binding.tvCounter.text = "${length.coerceAtMost(5)}/5"
    }

    private fun updateButtonState(isValid: Boolean) {
        binding.btnNext.isEnabled = isValid
    }

    companion object {
        private const val NAME_PATTERN = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]{1,5}$"
        private val nameRegex = Regex(NAME_PATTERN)
    }
}
