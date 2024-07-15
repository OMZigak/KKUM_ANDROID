package com.teamkkumul.feature.meetupcreate

import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateBinding
import com.teamkkumul.feature.utils.Debouncer
import com.teamkkumul.feature.utils.animateProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class MeetUpCreateFragment :
    BindingFragment<FragmentMeetUpCreateBinding>(R.layout.fragment_meet_up_create) {
    private val viewModel: MeetUpCreateViewModel by activityViewModels<MeetUpCreateViewModel>()
    private val meetUpCreateDebouncer = Debouncer<String>()
    private var currentText: String = ""

    override fun initView() {
        viewModel.setProgressBar(25)
        binding.clMeetUpDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.clMeetUpTime.setOnClickListener {
            showTimePickerDialog()
        }
        setName()
        binding.clMyGroupEnterLocation.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_meet_up_create_to_fragment_meet_up_create_location)
        }
        binding.tvNextToMeetUpCreateFriend.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_meet_up_create_to_fragment_meet_up_create_friend)
        }
        observeMeetUpDate()
        observeMeetUpTime()
        observeProgress()
        observeSelectedLocation()
    }

    private fun observeSelectedLocation() {
        viewModel.meetUpLocation.observe(viewLifecycleOwner) { location ->
            binding.tvMeetUpCreateLocationEnter.text = location
        }
    }

    private fun observeProgress() {
        val progressBar = binding.pbMeetUpCreate
        viewModel.progressLiveData.observe(viewLifecycleOwner) { progress ->
            progressBar.progress = progress
            animateProgressBar(progressBar, 0, progress)
        }
    }

    private fun setName() = with(binding.etMeetUpName) {
        doAfterTextChanged {
            meetUpCreateDebouncer.setDelay(
                text.toString(),
                SET_NAME_DEBOUNCE_DELAY,
                ::validInput,
            )
        }
        setOnEditorActionListener { _, actionId, event ->
            (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER))
        }
    }

    private fun validInput(input: String) {
        val isValid = input.length <= GROUP_NAME_MAX_LENGTH && input.matches(
            nameRegex,
        )
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
            tilMeetUpName.error = errorMessage
            tilMeetUpName.isErrorEnabled = errorMessage != null
        }
    }

    private fun setColor(colorResId: Int) {
        val color = colorOf(colorResId)
        with(binding) {
            tvCounter.setTextColor(color)
            etMeetUpName.setTextColor(color)
            tilMeetUpName.boxStrokeColor = color
        }
    }

    private fun updateCounter(length: Int) {
        binding.tvCounter.text = "${length.coerceAtMost(10)}/10"
    }

    private fun updateButtonState(isValid: Boolean) {
        binding.tvNextToMeetUpCreateFriend.isEnabled = isValid
    }

    private fun showDatePickerDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener { selectedDate ->
            val formattedDate = SimpleDateFormat("yyyy.MM.dd").format(Date(selectedDate))
            binding.tvMeetUpCreateDateEnter.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray8,
                ),
            )
            binding.ivMeetUpDate.setImageResource(R.drawable.ic_date_fill_24)
            viewModel.setMeetUpDate(formattedDate)
        }
        picker.show(parentFragmentManager, picker.toString())
    }

    private fun observeMeetUpDate() {
        viewModel.meetUpDate.flowWithLifecycle(viewLifeCycle).onEach {
            if (it.isNotEmpty()) {
                binding.tvMeetUpCreateDateEnter.text = it
                binding.tvMeetUpCreateDateEnter.setTextColor(colorOf(R.color.gray8))
                binding.ivMeetUpDate.setImageResource(R.drawable.ic_date_fill_24)
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hour)
            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
            .setMinute(minute)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val isPM = timePicker.hour >= 12
            val formattedTime = String.format(
                "%s %02d:%02d",
                if (isPM) "PM" else "AM",
                timePicker.hour % 12,
                timePicker.minute,
            )
            binding.tvMeetUpCreateTimeEnter.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray8,
                ),
            )
            binding.ivMeetUpTime.setImageResource(R.drawable.ic_time_fill_24)
            viewModel.setMeetUpTime(formattedTime)
        }
        timePicker.show(parentFragmentManager, timePicker.toString())
    }

    private fun observeMeetUpTime() {
        viewModel.meetUpTime.flowWithLifecycle(viewLifeCycle).onEach {
            if (it.isNotEmpty()) {
                binding.tvMeetUpCreateTimeEnter.text = it
                binding.tvMeetUpCreateTimeEnter.setTextColor(colorOf(R.color.gray8))
                binding.ivMeetUpTime.setImageResource(R.drawable.ic_time_fill_24)
            }
        }.launchIn(viewLifeCycleScope)
    }

    companion object {
        private const val NAME_PATTERN = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣\\s]{1,10}$"
        private val nameRegex = Regex(NAME_PATTERN)
        private const val SET_NAME_DEBOUNCE_DELAY = 300L
        private const val GROUP_NAME_MAX_LENGTH = 10
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setMeetUpDate("")
        viewModel.setMeetUpTime("")
    }
}
