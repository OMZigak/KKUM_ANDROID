package com.teamkkumul.feature.profile

import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentSetNameBinding

class SetNameFragment : BindingFragment<FragmentSetNameBinding>(R.layout.fragment_set_name) {
    override fun initView() {
        with(binding) {
            etSetName.addTextChangedListener(object : TextWatcher {
                private var currentText = ""

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
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
                            resetColors()
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

                private fun resetColors() {
                    setColor(R.color.main_color)
                }

                private fun setErrorState(filteredInput: String, input: String) {
                    when {
                        filteredInput.length > 5 || filteredInput != input -> {
                            tilSetName.error = "한글, 영문, 숫자만을 사용해 총 5자 이내로 입력해주세요."
                            tilSetName.isErrorEnabled = true
                            setColor(R.color.red)
                        }
                        else -> {
                            tilSetName.error = null
                            tilSetName.isErrorEnabled = false
                            setColor(R.color.main_color)
                        }
                    }
                }

                private fun setColor(colorResId: Int) {
                    val color = ContextCompat.getColor(requireContext(), colorResId)
                    tvCounter.setTextColor(color)
                    etSetName.setTextColor(color)
                    tilSetName.boxStrokeColor = color
                }

                private fun updateCounter(length: Int) {
                    tvCounter.text = "${length.coerceAtMost(5)}/5"
                }

                private fun updateButtonState() {
                    val isValid = tilSetName.error == null
                    btnNext.isEnabled = isValid
                }
            })
        }
    }
}
