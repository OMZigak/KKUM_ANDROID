package com.teamkkumul.feature.profile

import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentSetNameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetNameFragment : BindingFragment<FragmentSetNameBinding>(R.layout.fragment_set_name) {

    private val setNameViewModel: SetNameViewModel by activityViewModels()
    override fun initView() {
        setName()
        binding.btnNext.setOnClickListener {
            setNameViewModel.getInputName(binding.etSetName.text.toString())
            findNavController().navigate(R.id.action_fragment_set_name_to_fragment_set_profile)
        }
    }

    private fun setName() {
        with(binding) {
            etSetName.addTextChangedListener(object : TextWatcher {
                private var currentText = ""

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val input = it.toString()
                        val filteredInput = input.filter { char ->
                            char.isLetterOrDigit() || char.toString().matches(Regex("[ㄱ-ㅎㅏ-ㅣ가-힣]"))
                        }

                        if (filteredInput != input || filteredInput.length > 5) {
                            resetInput()
                            setErrorState(filteredInput, input)
                        } else {
                            currentText = filteredInput
                            setColor(R.color.main_color)
                            setErrorState(filteredInput, input)
                        }

                        updateCounter(filteredInput.length)
                        updateButtonState()
                    }
                }

                private fun resetInput() {
                    etSetName.removeTextChangedListener(this)
                    etSetName.setText(currentText)
                    etSetName.setSelection(currentText.length)
                    etSetName.addTextChangedListener(this)
                }
            })
        }
    }

    private fun setErrorState(filteredInput: String, input: String) {
        with(binding) {
            if (filteredInput.length > 5 || filteredInput != input) {
                tilSetName.error = "한글, 영문, 숫자만을 사용해 총 5자 이내로 입력해주세요."
                tilSetName.isErrorEnabled = true
                setColor(R.color.red)
            } else {
                tilSetName.error = null
                tilSetName.isErrorEnabled = false
                setColor(R.color.main_color)
            }
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

    private fun updateButtonState() {
        with(binding) {
            val isValid = tilSetName.error == null
            btnNext.isEnabled = isValid
        }
    }
}
