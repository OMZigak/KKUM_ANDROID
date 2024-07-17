package com.teamkkumul.feature.signup

import android.content.Intent
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamkkumul.core.ui.base.BindingActivity
import com.teamkkumul.core.ui.util.context.colorOf
import com.teamkkumul.core.ui.util.context.hideKeyboard
import com.teamkkumul.core.ui.util.context.toast
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.ActivitySetNameBinding
import com.teamkkumul.feature.utils.Debouncer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SetNameActivity : BindingActivity<ActivitySetNameBinding>(R.layout.activity_set_name) {

    private val setNameViewModel: SetNameViewModel by viewModels()
    private val setNameDebouncer = Debouncer<String>()
    private var currentText: String = ""

    override fun initView() {
        setName()
        binding.btnNext.setOnClickListener {
            val inputName = binding.etSetName.text.toString()
            setNameViewModel.updateName(inputName)
            navigateToSetProfile(inputName)
        }
        binding.clSetName.setOnClickListener {
            hideKeyboard(binding.clSetName)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        setNameViewModel.updateNameState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {}
                is UiState.Failure -> {}
                is UiState.Loading -> {}
                UiState.Empty -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun setName() = with(binding.etSetName) {
        doAfterTextChanged {
            setNameDebouncer.setDelay(text.toString(), SET_NAME_DEBOUNCE_DELAY, ::validInput)
        }
        setOnEditorActionListener { _, actionId, event ->
            (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER))
        }
    }

    private fun validInput(input: String) {
        val isValid = input.length <= NAME_MAX_LENGTH && input.matches(nameRegex)
        if (isValid) {
            currentText = input
            setColor(R.color.main_color)
            setErrorState(null)
        } else {
            setColor(R.color.red)
            setErrorState(getString(R.string.set_name_error_message))
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
        val color = colorOf(colorResId)
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

    private fun navigateToSetProfile(inputName: String) {
        val intent = Intent(this, SetProfileActivity::class.java).apply {
            putExtra(INPUT_NAME, inputName)
        }
        startActivity(intent)
    }

    companion object {
        private const val NAME_PATTERN = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]{1,5}$"
        private val nameRegex = Regex(NAME_PATTERN)
        private const val SET_NAME_DEBOUNCE_DELAY = 300L
        private const val NAME_MAX_LENGTH = 5
        const val INPUT_NAME = "inputName"
    }
}
