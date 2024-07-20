package com.teamkkumul.feature.meetupcreate

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.hideKeyboard
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateBinding
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.animateProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MeetUpCreateFragment :
    BindingFragment<FragmentMeetUpCreateBinding>(R.layout.fragment_meet_up_create) {
    private val viewModel: MeetUpCreateViewModel by activityViewModels<MeetUpCreateViewModel>()
    private var currentText: String = ""

    override fun initView() {
        val id = arguments?.getInt(KeyStorage.MEETING_ID) ?: -1

        viewModel.setProgressBar(25)
        binding.clMeetUpDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.clMeetUpTime.setOnClickListener {
            showTimePickerDialog()
        }
        setName()
        binding.clMyGroupEnterLocation.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_meet_up_create_to_fragment_meet_up_create_location,
            )
        }
        observeMeetUpDate()
        observeMeetUpTime()
        observeProgress()
        observeSelectedLocation()
        observeFormValidation()
        navigateToFriend(id)
        initHideKeyBoard()
    }

    private fun observeFormValidation() {
        viewModel.meetUpInputState.flowWithLifecycle(viewLifeCycle).onEach { isFormValid ->
            binding.btnMeetUpCreateNext.isEnabled = isFormValid
        }.launchIn(viewLifeCycleScope)
    }

    private fun navigateToFriend(id: Int) {
        binding.btnMeetUpCreateNext.setOnClickListener {
            val meetUpDate = "${viewModel.meetUpDate.value} ${viewModel.meetUpTime.value}" ?: ""
            val meetUpTime = viewModel.meetUpTime.value ?: ""
            val meetUpLocation = viewModel.meetUpLocation.value ?: ""
            val meetUpName = currentText

            val bundle = Bundle().apply {
                putInt(KeyStorage.MEETING_ID, id)
                putString(KeyStorage.MEET_UP_DATE, meetUpDate)
                putString(KeyStorage.MEET_UP_TIME, meetUpTime)
                putString(KeyStorage.MEET_UP_LOCATION, meetUpLocation)
                putString(KeyStorage.MEET_UP_NAME, meetUpName)
            }

            findNavController().navigate(
                R.id.action_fragment_meet_up_create_to_fragment_meet_up_create_friend,
                bundle,
            )
        }
    }

    private fun observeSelectedLocation() {
        viewModel.meetUpLocation.flowWithLifecycle(viewLifeCycle).onEach { location ->
            binding.tvMeetUpCreateLocationEnter.text = location
        }.launchIn(viewLifeCycleScope)
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
            validInput(text.toString())
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
            setErrorState(getString(R.string.meet_up_name_error_message))
        }
        updateCounter(input.length)
        viewModel.setMeetUpName(isValid)
    }

    private fun initHideKeyBoard() {
        binding.root.setOnClickListener {
            requireContext().hideKeyboard(binding.root)
        }
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

    private fun showDatePickerDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener { selectedDate ->
            val formattedDate = SimpleDateFormat("yyyy.MM.dd").format(Date(selectedDate))
            val formattedDateForm =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(selectedDate))

            Timber.tag("day").d(formattedDateForm)

            binding.tvMeetUpCreateDateEnter.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray8,
                ),
            )
            binding.ivMeetUpDate.setImageResource(R.drawable.ic_date_fill_24)
            viewModel.setMeetUpDate(formattedDateForm)
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

            val formattedTimeForm = String.format(
                "%02d:%02d:%02d",
                timePicker.hour,
                timePicker.minute,
                0,
            )

            Timber.tag("day").d(formattedTimeForm)

            binding.tvMeetUpCreateTimeEnter.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray8,
                ),
            )
            binding.ivMeetUpTime.setImageResource(R.drawable.ic_time_fill_24)
            viewModel.setMeetUpTime(formattedTimeForm)
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
        viewModel.setMeetUpLocation("")
    }
}
