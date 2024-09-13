package com.teamkkumul.feature.meetup.readystatus.readyinfoinput

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.hideKeyboard
import com.teamkkumul.core.ui.util.fragment.colorOf
import com.teamkkumul.core.ui.util.fragment.toast
import com.teamkkumul.core.ui.util.fragment.viewLifeCycle
import com.teamkkumul.core.ui.util.fragment.viewLifeCycleScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.core.ui.view.doAfterTextChangedWithCursor
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentReadyInfoInputBinding
import com.teamkkumul.feature.meetup.readystatus.readyinfoinput.alarm.AlarmReceiver
import com.teamkkumul.feature.utils.Debouncer
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.TimeStorage
import com.teamkkumul.feature.utils.time.calculateReadyStartTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Calendar

@AndroidEntryPoint
class ReadyInfoInputFragment :
    BindingFragment<FragmentReadyInfoInputBinding>(R.layout.fragment_ready_info_input) {
    private val viewModel: ReadyInfoInputViewModel by viewModels()

    private val setTimeDebouncer = Debouncer<String>()

    private val promiseId: Int by lazy {
        requireArguments().getInt(PROMISE_ID)
    }

    private val promiseTime: String by lazy {
        requireArguments().getString("promiseTime").orEmpty()
    }

    private var promiseName: String? = null

    override fun initView() {
        initSetEditText()
        initObserveState()
        initReadyInputBtnClick()
        initHideKeyBoard()
    }

    private fun initSetEditText() {
        setReadyHour()
        setReadyMinute()
        setMovingHour()
        setMovingMinute()
    }

    private fun initObserveState() {
        observeMeetUpDetailState()
        observeNextBtnState()
        observeReadyInfoState()
    }

    private fun observeMeetUpDetailState() {
        viewModel.getMeetUpDetail(promiseId)
        viewModel.meetUpDetailState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    promiseName = state.data.promiseName
                }

                is UiState.Failure -> Timber.e(state.toString())
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun observeNextBtnState() {
        viewModel.readyInputState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            binding.btnReadyInfoNext.isEnabled = state
        }.launchIn(viewLifeCycleScope)
    }

    private fun setReadyHour() {
        binding.etReadyStatusReadHour.doAfterTextChangedWithCursor { text ->
            setTimeDebouncer.setDelay(text, 40L) { delayedText ->
                viewModel.setReadyHour(delayedText)
            }
        }
    }

    private fun setReadyMinute() {
        binding.etReadyStatusReadyMinute.doAfterTextChangedWithCursor { text ->
            setTimeDebouncer.setDelay(text, 40L) { delayedText ->
                viewModel.setReadyMinute(delayedText)
            }
        }
    }

    private fun setMovingHour() {
        binding.etReadyStatusMovingHour.doAfterTextChangedWithCursor { text ->
            setTimeDebouncer.setDelay(text, 40L) { delayedText ->
                viewModel.setMovingHour(delayedText)
            }
        }
    }

    private fun setMovingMinute() {
        binding.etReadyStatusMovingMinute.doAfterTextChangedWithCursor { text ->
            setTimeDebouncer.setDelay(text, 40L) { delayedText ->
                viewModel.setMovingMinute(delayedText)
            }
        }
    }

    private fun observeReadyInfoState() {
        viewModel.readyInfo.flowWithLifecycle(viewLifeCycle).onEach { readyInfo ->
            updateReadyHourState(readyInfo.readyHour)
            updateReadyMinuteState(readyInfo.readyMinute)
            updateMovingHourState(readyInfo.movingHour)
            updateMovingMinuteState(readyInfo.movingMinute)
        }.launchIn(viewLifeCycleScope)
    }

    private fun updateReadyHourState(isValid: Boolean?) = with(binding) {
        val color =
            if (isValid ?: return) colorOf(R.color.main_color) else colorOf(R.color.red)
        tilReadyStatusReadHour.boxStrokeColor = color
        etReadyStatusReadHour.setTextColor(color)
        if (!isValid) {
            etReadyStatusReadHour.setText(TimeStorage.END_HOUR.toString())
            toast(getString(R.string.ready_info_input_hour_error))
        }
    }

    private fun updateReadyMinuteState(isValid: Boolean?) = with(binding) {
        val color =
            if (isValid ?: return) colorOf(R.color.main_color) else colorOf(R.color.red)
        tilReadyStatusReadyMinute.boxStrokeColor = color
        etReadyStatusReadyMinute.setTextColor(color)
        if (!isValid) {
            etReadyStatusReadyMinute.setText(TimeStorage.END_MINUTE.toString())
            toast(getString(R.string.ready_info_input_minute_error))
        }
    }

    private fun updateMovingHourState(isValid: Boolean?) = with(binding) {
        val color =
            if (isValid ?: return) colorOf(R.color.main_color) else colorOf(R.color.red)
        tilReadyStatusMovingHour.boxStrokeColor = color
        etReadyStatusMovingHour.setTextColor(color)
        if (!isValid) {
            etReadyStatusMovingHour.setText(TimeStorage.END_HOUR.toString())
            toast(getString(R.string.ready_info_input_hour_error))
        }
    }

    private fun updateMovingMinuteState(isValid: Boolean?) = with(binding) {
        val color =
            if (isValid ?: return) colorOf(R.color.main_color) else colorOf(R.color.red)
        tilReadyStatusMovingMinute.boxStrokeColor = color
        etReadyStatusMovingMinute.setTextColor(color)
        if (!isValid) {
            etReadyStatusMovingMinute.setText(TimeStorage.END_MINUTE.toString())
            toast(getString(R.string.ready_info_input_minute_error))
        }
    }

    private fun initReadyInputBtnClick() {
        binding.btnReadyInfoNext.setOnClickListener {
            val readyHour = binding.etReadyStatusReadHour.text.toString().toIntOrNull() ?: 0
            val readyMinute = binding.etReadyStatusReadyMinute.text.toString().toIntOrNull() ?: 0
            val movingHour = binding.etReadyStatusMovingHour.text.toString().toIntOrNull() ?: 0
            val movingMinute = binding.etReadyStatusMovingMinute.text.toString().toIntOrNull() ?: 0

            val readyTime = calculateFutureTime(readyHour, readyMinute)
            val movingTime = calculateFutureTime(movingHour, movingMinute)

            viewModel.patchReadyInfoInput(promiseId, readyTime, movingTime)
            observePatchReadyInfoInputState(readyTime, movingTime)
        }
    }

    private fun observePatchReadyInfoInputState(readyTime: Int, movingTime: Int) {
        viewModel.patchReadyInfoInputState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    findNavController().navigate(
                        R.id.action_fragment_ready_info_input_to_readyInputCompletedFragment,
                        bundleOf(PROMISE_ID to promiseId),
                    )
                    setReadyAndMovingAlarms(
                        promiseName,
                        readyTime,
                        movingTime,
                    )
                }

                is UiState.Failure -> toast(state.errorMessage)
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun setReadyAndMovingAlarms(
        promiseName: String?,
        readyTime: Int,
        movingTime: Int,
    ) {
        val readyAlarmTime = calculateReadyStartTime(promiseTime, readyTime, movingTime) ?: return
        val movingAlarmTime = calculateReadyStartTime(promiseTime, 0, movingTime) ?: return
        // 준비 시작 알람 설정
        setAlarm(
            readyAlarmTime,
            getString(R.string.ready_info_input_ready_title),
            getString(R.string.ready_info_input_ready_content, promiseName),
            0,
        )

        // 이동 시작 알람 설정
        setAlarm(
            movingAlarmTime,
            getString(R.string.ready_info_input_moving_title),
            getString(R.string.ready_info_input_moving_content, promiseName),
            1,
        )
    }

    private fun calculateFutureTime(hour: Int, minute: Int): Int {
        return hour * 60 + minute
    }

    private fun setAlarm(
        alarmCalendar: Calendar,
        alarmTitle: String,
        alarmContent: String,
        requestCode: Int,
    ) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra(KeyStorage.ALARM_TITLE, alarmTitle)
            putExtra(KeyStorage.ALARM_CONTENT, alarmContent)
            putExtra(KeyStorage.TAB_INDEX, 1)
            putExtra(PROMISE_ID, promiseId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmCalendar.timeInMillis,
            pendingIntent,
        )
    }

    private fun initHideKeyBoard() {
        binding.root.setOnClickListener {
            requireContext().hideKeyboard(binding.root)
        }
    }
}
