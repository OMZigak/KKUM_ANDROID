package com.teamkkumul.feature.meetupcreate

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.hideKeyboard
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentMeetUpCreateBinding
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.animateProgressBar
import com.teamkkumul.feature.utils.time.TimeUtils.changeDateToText
import com.teamkkumul.feature.utils.time.TimeUtils.changeTimeToPmAm
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
    private val sharedViewModel: MeetUpSharedViewModel by activityViewModels<MeetUpSharedViewModel>()
    private val viewModel: MeetUpCreateViewModel by viewModels<MeetUpCreateViewModel>()
    private var isInitialized = false
    private var timeEntered = false
    private var dateEntered = false

    override fun initView() {
        val meetingId = arguments?.getInt(KeyStorage.MEETING_ID) ?: -1

        if (!isInitialized && sharedViewModel.isEditMode()) {
            initEditFlow()
            isInitialized = true
            binding.tbMeetUpCreate.title = getString(R.string.edit_meet_up_title)
            navigateToEditFriend(sharedViewModel.getPromiseId())
        } else {
            binding.tbMeetUpCreate.title = getString(R.string.create_meet_up_title)
            navigateToFriend(meetingId)
            Timber.tag("sS").d(getString(R.string.create_meet_up_title))
        }

        sharedViewModel.setProgressBar(25)
        binding.clMeetUpDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.clMeetUpTime.setOnClickListener {
            showTimePickerDialog()
            observeMeetUpTime()
        }
        setName()
        binding.clMyGroupEnterLocation.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_meet_up_create_to_fragment_meet_up_create_location,
            )
        }
        binding.tbMeetUpCreate.toolbarMyPageLine.visibility = View.GONE

        observeMeetUpDate()
        observeMeetUpTime()
        observeProgress()
        observeSelectedLocation()
        observeFormValidation()
        initHideKeyBoard()
    }

    private fun initEditFlow() {
        binding.etMeetUpName.setText(sharedViewModel.meetUpCreateModel.value.name)
        validInput(sharedViewModel.meetUpCreateModel.value.name)
        binding.tvMeetUpCreateDateEnter.text =
            sharedViewModel.meetUpCreateModel.value.date?.changeDateToText()
        binding.tvMeetUpCreateDateEnter.text =
            sharedViewModel.meetUpCreateModel.value.date?.changeDateToText()
        binding.tvMeetUpCreateTimeEnter.text =
            sharedViewModel.meetUpCreateModel.value.time.changeTimeToPmAm()
        binding.tvMeetUpCreateLocationEnter.text = sharedViewModel.meetUpCreateModel.value.placeName
    }

    private fun observeFormValidation() {
        viewModel.meetUpInputState.flowWithLifecycle(viewLifeCycle).onEach {
            binding.btnMeetUpCreateNext.isEnabled = isFormComplete()
        }.launchIn(viewLifeCycleScope)
    }

    private fun navigateToEditFriend(promiseId: Int) {
        binding.btnMeetUpCreateNext.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_meet_up_create_to_fragment_meet_up_create_friend,
            )
        }
    }

    private fun navigateToFriend(id: Int) {
        binding.btnMeetUpCreateNext.setOnClickListener {
            sharedViewModel.updateMeetUpModel(meetingId = id)
            findNavController().navigate(
                R.id.action_fragment_meet_up_create_to_fragment_meet_up_create_friend,
            )
        }
    }

    private fun observeSelectedLocation() {
        sharedViewModel.meetUpCreateModel.flowWithLifecycle(viewLifeCycle).onEach {
            binding.tvMeetUpCreateLocationEnter.text = it.placeName
        }.launchIn(viewLifeCycleScope)
    }

    private fun observeProgress() {
        val progressBar = binding.pbMeetUpCreate
        sharedViewModel.progressLiveData.observe(viewLifecycleOwner) { progress ->
            progressBar.progress = progress
            animateProgressBar(progressBar, 0, progress)
        }
    }

    private fun setName() = with(binding.etMeetUpName) {
        setSingleLine(true)
        doAfterTextChanged {
            validInput(text.toString())
        }
        setOnEditorActionListener { _, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                requireContext().hideKeyboard(this)
                true
            } else {
                false
            }
        }
    }

    private fun validInput(input: String) {
        val isValid = input.length <= GROUP_NAME_MAX_LENGTH &&
            input.matches(
                nameRegex,
            )
        if (isValid) {
            sharedViewModel.updateMeetUpModel(name = input)
            setColor(R.color.main_color)
            setInputTextColor(R.color.black0)
            setErrorState(null)
            viewModel.setMeetUpName(true)
        } else {
            setColor(R.color.red)
            setInputTextColor(R.color.red)
            setErrorState(getString(R.string.meet_up_name_error_message))
        }
        updateCounter(input.length)
        viewModel.setMeetUpName(isValid)
        binding.btnMeetUpCreateNext.isEnabled = isFormComplete()
    }

    private fun setInputTextColor(colorResId: Int) {
        binding.etMeetUpName.setTextColor(colorOf(colorResId))
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
            tilMeetUpName.boxStrokeColor = color
        }
    }

    private fun updateCounter(length: Int) {
        binding.tvCounter.text = "$length/10"
    }

    private fun showDatePickerDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener { selectedDate ->
            Timber.tag("create time").d(selectedDate.toString())
            val formattedDateForm =
                formatDate(Date(selectedDate), "yyyy-MM-dd")

            binding.tvMeetUpCreateDateEnter.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray8,
                ),
            )
            binding.ivMeetUpDate.setImageResource(R.drawable.ic_date_fill_24)
            sharedViewModel.updateMeetUpModel(date = formattedDateForm)
        }
        picker.show(parentFragmentManager, picker.toString())
    }

    private fun observeMeetUpDate() {
        sharedViewModel.meetUpCreateModel.flowWithLifecycle(viewLifeCycle).onEach {
            if (!it.date.isNullOrEmpty()) {
                binding.tvMeetUpCreateDateEnter.text = it.date.toString().changeDateToText()
                binding.tvMeetUpCreateDateEnter.setTextColor(colorOf(R.color.gray8))
                binding.ivMeetUpDate.setImageResource(R.drawable.ic_date_fill_24)
                dateEntered = true
                observeFormValidation()
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun formatDate(date: Date, format: String = "yyyy-MM-dd"): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
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
            val formattedTimeForm = String.format(
                "%02d:%02d:%02d",
                timePicker.hour,
                timePicker.minute,
                0,
            )
            binding.tvMeetUpCreateTimeEnter.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray8,
                ),
            )
            binding.ivMeetUpTime.setImageResource(R.drawable.ic_time_fill_24)
            sharedViewModel.updateMeetUpModel(time = formattedTimeForm)
        }
        timePicker.show(parentFragmentManager, timePicker.toString())
    }

    private fun observeMeetUpTime() {
        sharedViewModel.meetUpCreateModel.flowWithLifecycle(viewLifeCycle).onEach {
            if (it.time.isNotEmpty()) {
                binding.tvMeetUpCreateTimeEnter.text = it.time.changeTimeToPmAm()
                binding.tvMeetUpCreateTimeEnter.setTextColor(colorOf(R.color.gray8))
                binding.ivMeetUpTime.setImageResource(R.drawable.ic_time_fill_24)
                timeEntered = true
                observeFormValidation()
            }
            Timber.tag("time").d(timeEntered.toString())
        }.launchIn(viewLifeCycleScope)
    }

    private fun isFormComplete(): Boolean {
        return viewModel.meetUpName.value &&
            timeEntered &&
            dateEntered &&
            binding.tvMeetUpCreateLocationEnter.text.isNotEmpty()
    }

    companion object {
        private const val NAME_PATTERN = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣\\s]{1,10}$"
        private val nameRegex = Regex(NAME_PATTERN)
        private const val GROUP_NAME_MAX_LENGTH = 10
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.updateMeetUpModel(time = "")
        sharedViewModel.updateMeetUpModel(date = "")
        sharedViewModel.updateMeetUpModel(placeName = "")
        sharedViewModel.updateMeetUpModel(name = "")
    }
}
