package com.teamkkumul.feature.meetupcreate

import android.os.Bundle
import android.view.View
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
import com.teamkkumul.feature.utils.MeetUpType
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
    private var isInitialized = false

    override fun initView() {
        val meetUpType =
            arguments?.getString(KeyStorage.MEET_UP_TYPE) ?: MeetUpType.CREATE.name
        val id = arguments?.getInt(KeyStorage.MEETING_ID) ?: -1
        val promiseId = arguments?.getInt(KeyStorage.PROMISE_ID) ?: -1
        viewModel.updateMeetUpModel(
            promiseId = promiseId,
            meetupType = meetUpType,
        ) // 특정 필드만 updatae 하기

        if (viewModel.isEditMode()) {
            arguments?.let {
                initEditFlow(it)
            }
            isInitialized = true
            binding.tbMeetUpCreate.toolbarTitle.text = getString(R.string.edit_meet_up_title)
            navigateToEditFriend(viewModel.getPromiseId()) // viewModel.getPromiseId() =  promise id를 가져옴
        } else {
            binding.tbMeetUpCreate.toolbarTitle.text = getString(R.string.create_meet_up_title)

            navigateToFriend(id)
        }

        Timber.tag("ce").d(meetUpType)
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

        binding.tbMeetUpCreate.toolbarMyPageLine.visibility = View.GONE

        observeMeetUpDate()
        observeMeetUpTime()
        observeProgress()
        observeSelectedLocation()
        observeFormValidation()
        initHideKeyBoard()
    }

    private fun initEditFlow(arguments: Bundle) {
        currentText = arguments.getString(KeyStorage.MEET_UP_NAME, "")
        viewModel.setMeetUpDate(arguments.getString(KeyStorage.MEET_UP_DATE, ""))
        viewModel.setMeetUpTime(arguments.getString(KeyStorage.MEET_UP_TIME, ""))
        viewModel.setMeetUpLocation(arguments.getString(KeyStorage.MEET_UP_LOCATION, ""))
        viewModel.setMeetUpLocationX(arguments.getString(KeyStorage.MEET_UP_LOCATION_X, ""))
        viewModel.setMeetUpLocationY(arguments.getString(KeyStorage.MEET_UP_LOCATION_Y, ""))

        binding.etMeetUpName.setText(currentText)
        validInput(currentText)
        binding.tvMeetUpCreateDateEnter.text = viewModel.meetUpDate.value
        binding.tvMeetUpCreateTimeEnter.text = viewModel.meetUpTime.value
        binding.tvMeetUpCreateLocationEnter.text = viewModel.meetUpLocation.value
    }

    private fun observeFormValidation() {
        viewModel.meetUpInputState.flowWithLifecycle(viewLifeCycle).onEach { isFormValid ->
            binding.btnMeetUpCreateNext.isEnabled = isFormValid
        }.launchIn(viewLifeCycleScope)
    }

    private fun navigateToEditFriend(promiseId: Int) {
        binding.btnMeetUpCreateNext.setOnClickListener {
            val meetUpDate = "${viewModel.meetUpDate.value} ${viewModel.meetUpTime.value}" ?: ""
            val meetUpTime = viewModel.meetUpTime.value ?: ""
            val meetUpLocation = viewModel.meetUpLocation.value ?: ""
            val meetUpName = currentText
            val meetUpLocationX = viewModel.meetUpLocationX.value
            val meetUpLocationY = viewModel.meetUpLocationY.value

            val bundle = Bundle().apply {
                putString(KeyStorage.MEET_UP_DATE, meetUpDate)
                putString(KeyStorage.MEET_UP_TIME, meetUpTime)
                putString(KeyStorage.MEET_UP_LOCATION, meetUpLocation)
                putString(KeyStorage.MEET_UP_NAME, meetUpName)
                putString(KeyStorage.MEET_UP_LOCATION_X, meetUpLocationX)
                putString(KeyStorage.MEET_UP_LOCATION_Y, meetUpLocationY)
            }

            findNavController().navigate(
                R.id.action_fragment_meet_up_create_to_fragment_meet_up_create_friend,
                bundle,
            )
            Timber.tag("promise edit").d(bundle.toString())
        }
    }

    private fun navigateToFriend(id: Int) {
        binding.btnMeetUpCreateNext.setOnClickListener {
            val meetUpDate = "${viewModel.meetUpDate.value} ${viewModel.meetUpTime.value}" ?: ""
            val meetUpTime = viewModel.meetUpTime.value ?: ""
            val meetUpLocation = viewModel.meetUpLocation.value ?: ""
            val meetUpName = currentText
            val meetUpLocationX = viewModel.meetUpLocationX.value
            val meetUpLocationY = viewModel.meetUpLocationY.value

            val bundle = Bundle().apply {
                putInt(KeyStorage.MEETING_ID, id)
                putString(KeyStorage.MEET_UP_DATE, meetUpDate)
                putString(KeyStorage.MEET_UP_TIME, meetUpTime)
                putString(KeyStorage.MEET_UP_LOCATION, meetUpLocation)
                putString(KeyStorage.MEET_UP_NAME, meetUpName)
                putString(KeyStorage.MEET_UP_LOCATION_X, meetUpLocationX)
                putString(KeyStorage.MEET_UP_LOCATION_Y, meetUpLocationY)
                putString(KeyStorage.MEET_UP_TYPE, arguments?.getString(KeyStorage.MEET_UP_TYPE))
            }

            findNavController().navigate(
                R.id.action_fragment_meet_up_create_to_fragment_meet_up_create_friend,
                bundle,
            )
            Timber.tag("promise").d(bundle.toString())
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
            currentText = input
            setColor(R.color.main_color)
            setInputTextColor(R.color.black0)
            setErrorState(null)
        } else {
            setColor(R.color.red)
            setInputTextColor(R.color.red)
            setErrorState(getString(R.string.meet_up_name_error_message))
        }
        updateCounter(input.length)
        viewModel.setMeetUpName(isValid)
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
            viewModel.setMeetUpDate(formattedDateForm)
        }
        picker.show(parentFragmentManager, picker.toString())
    }

    private fun observeMeetUpDate() {
        viewModel.meetUpDate.flowWithLifecycle(viewLifeCycle).onEach {
            if (it.isNotEmpty()) {
                Timber.tag("create date").d(it)
                val date = parseDate(it, "yyyy-MM-dd")
                val formattedDate = date?.let { date -> formatDate(date, "yyyy.MM.dd") }

                binding.tvMeetUpCreateDateEnter.text = formattedDate
                binding.tvMeetUpCreateDateEnter.setTextColor(colorOf(R.color.gray8))
                binding.ivMeetUpDate.setImageResource(R.drawable.ic_date_fill_24)
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun formatDate(date: Date, format: String = "yyyy-MM-dd"): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }

    private fun parseDate(dateString: String, format: String = "yyyy-MM-dd"): Date? {
        return SimpleDateFormat(format, Locale.getDefault()).parse(dateString)
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
            viewModel.setMeetUpTime(formattedTimeForm)
        }
        timePicker.show(parentFragmentManager, timePicker.toString())
    }

    private fun observeMeetUpTime() {
        viewModel.meetUpTime.flowWithLifecycle(viewLifeCycle).onEach {
            if (it.isNotEmpty()) {
                val formattedTime = formatTime(it)
                binding.tvMeetUpCreateTimeEnter.text = formattedTime
                binding.tvMeetUpCreateTimeEnter.setTextColor(colorOf(R.color.gray8))
                binding.ivMeetUpTime.setImageResource(R.drawable.ic_time_fill_24)
            }
        }.launchIn(viewLifeCycleScope)
    }

    fun formatTime(timeString: String): String {
        val timeParts = timeString.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()
        val isPM = hour >= 12

        val formattedHour = if (hour % 12 == 0) 12 else hour % 12

        return String.format(
            "%s %02d:%02d",
            if (isPM) "PM" else "AM",
            formattedHour,
            minute,
        )
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
