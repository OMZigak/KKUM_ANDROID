package com.teamkkumul.feature.meetup.readystatus.readyinfoinput

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
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
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentReadyInfoInputBinding
import com.teamkkumul.feature.meetup.readystatus.readyinfoinput.alarm.AlarmReceiver
import com.teamkkumul.feature.utils.Debouncer
import com.teamkkumul.feature.utils.KeyStorage
import com.teamkkumul.feature.utils.KeyStorage.PROMISE_ID
import com.teamkkumul.feature.utils.TimeStorage
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
        requireArguments().getInt(KeyStorage.PROMISE_ID)
    }
    private var promiseName: String? = null

    override fun initView() {
        initHideKeyBoard()
        initSetEditText()
        initObserveState()
        initReadyInputBtnClick()
        initObserveMeetUpDetailState()
    }

    private fun initObserveMeetUpDetailState() {
        viewModel.getMeetUpDetail(promiseId)
        viewModel.meetUpDetailState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    promiseName = state.data.promiseName
                }

                is UiState.Failure -> Timber.tag("readyinput").d(state.toString())
                else -> {}
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initHideKeyBoard() {
        binding.root.setOnClickListener {
            requireContext().hideKeyboard(binding.root)
        }
    }

    private fun initSetEditText() {
        setReadyHour()
        setReadyMinute()
        setMovingHour()
        setMovingMinute()
    }

    private fun initObserveState() {
        observeNextBtnState()
        observeReadyHourState()
        observeReadyMinuteState()
        observeMovingHourState()
        observeMovingMinuteState()
    }

    private fun observeNextBtnState() {
        viewModel.readyInputState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            binding.btnReadyInfoNext.isEnabled = state
        }.launchIn(viewLifeCycleScope)
    }

    private fun setReadyHour() {
        binding.etReadyStatusReadHour.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                setTimeDebouncer.setDelay(text.toString(), 400L) { delayedText ->
                    viewModel.setReadyHour(delayedText)
                }
            }
        }
    }

    private fun setReadyMinute() {
        binding.etReadyStatusReadyMinute.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                setTimeDebouncer.setDelay(text.toString(), 400L) { delayedText ->
                    viewModel.setReadyMinute(delayedText)
                }
            }
        }
    }

    private fun setMovingHour() {
        binding.etReadyStatusMovingHour.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                setTimeDebouncer.setDelay(text.toString(), 400L) { delayedText ->
                    viewModel.setMovingHour(delayedText)
                }
            }
        }
    }

    private fun setMovingMinute() {
        binding.etReadyStatusMovingMinute.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty()) {
                setTimeDebouncer.setDelay(text.toString(), 400L) { delayedText ->
                    viewModel.setMovingMinute(delayedText)
                }
            }
        }
    }

    private fun observeReadyHourState() = with(binding) {
        viewModel.readyHour.flowWithLifecycle(viewLifeCycle).onEach { isValid ->
            val color = if (isValid) colorOf(R.color.main_color) else colorOf(R.color.red)
            tilReadyStatusReadHour.boxStrokeColor = color
            etReadyStatusReadHour.setTextColor(color)
            if (!isValid) {
                etReadyStatusReadHour.setText(TimeStorage.END_HOUR.toString())
                toast(getString(R.string.ready_info_input_hour_error))
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun observeReadyMinuteState() = with(binding) {
        viewModel.readyMinute.flowWithLifecycle(viewLifeCycle).onEach { isValid ->
            val color = if (isValid) colorOf(R.color.main_color) else colorOf(R.color.red)
            tilReadyStatusReadyMinute.boxStrokeColor = color
            etReadyStatusReadyMinute.setTextColor(color)
            if (!isValid) {
                etReadyStatusReadyMinute.setText(TimeStorage.END_MINUTE.toString())
                toast(getString(R.string.ready_info_input_minute_error))
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun observeMovingHourState() = with(binding) {
        viewModel.movingHour.flowWithLifecycle(viewLifeCycle).onEach { isValid ->
            val color = if (isValid) colorOf(R.color.main_color) else colorOf(R.color.red)
            tilReadyStatusMovingHour.boxStrokeColor = color
            etReadyStatusMovingHour.setTextColor(color)
            if (!isValid) {
                etReadyStatusMovingHour.setText(TimeStorage.END_HOUR.toString())
                toast(getString(R.string.ready_info_input_hour_error))
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun observeMovingMinuteState() = with(binding) {
        viewModel.movingMinute.flowWithLifecycle(viewLifeCycle).onEach { isValid ->
            val color = if (isValid) colorOf(R.color.main_color) else colorOf(R.color.red)
            tilReadyStatusMovingMinute.boxStrokeColor = color
            etReadyStatusMovingMinute.setTextColor(color)
            if (!isValid) {
                etReadyStatusMovingMinute.setText(TimeStorage.END_MINUTE.toString())
                toast(getString(R.string.ready_info_input_minute_error))
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initReadyInputBtnClick() {
        binding.btnReadyInfoNext.setOnClickListener {
            val readyHour = binding.etReadyStatusReadHour.text.toString().toIntOrNull() ?: 0
            val readyMinute = binding.etReadyStatusReadyMinute.text.toString().toIntOrNull() ?: 0
            val movingHour = binding.etReadyStatusMovingHour.text.toString().toIntOrNull() ?: 0
            val movingMinute = binding.etReadyStatusMovingMinute.text.toString().toIntOrNull() ?: 0

            // 준비 시작 알람 설정
            val readyTime = calculateFutureTime(readyHour, readyMinute)
            setAlarm(
                readyTime,
                getString(R.string.ready_info_input_ready_title),
                getString(R.string.ready_info_input_ready_content, promiseName),
                0,
                promiseId,
            )

            val movingTime = calculateFutureTime(movingHour, movingMinute)
            setAlarm(
                movingTime,
                getString(R.string.ready_info_input_moving_title),
                getString(R.string.ready_info_input_moving_content, promiseName),
                1,
                promiseId,
            )

            viewModel.patchReadyInfoInput(promiseId, readyTime, movingTime)

            viewModel.patchReadyInfoInputState.flowWithLifecycle(viewLifeCycle)
                .onEach { state ->
                    when (state) {
                        is UiState.Success -> findNavController().navigate(
                            R.id.action_fragment_ready_info_input_to_readyInputCompletedFragment,
                            bundleOf(PROMISE_ID to promiseId),
                        )

                        is UiState.Failure -> Timber.tag("readyinput").d(it.toString())
                        else -> {}
                    }
                }.launchIn(viewLifeCycleScope)
        }
    }

    private fun calculateFutureTime(hour: Int, minute: Int): Int {
        return hour * 60 + minute
    }

    private fun setAlarm(
        minutesFromNow: Int,
        alarmTitle: String,
        alarmContent: String,
        requestCode: Int,
        promiseId: Int, // 추가된 파라미터
    ) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.MINUTE, minutesFromNow)
            set(Calendar.SECOND, 0)
        }

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra(KeyStorage.ALARM_TITLE, alarmTitle)
            putExtra(KeyStorage.ALARM_CONTENT, alarmContent)
            putExtra(KeyStorage.TAB_INDEX, 1)
            putExtra(KeyStorage.PROMISE_ID, promiseId) // 추가된 부분
        }
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}
