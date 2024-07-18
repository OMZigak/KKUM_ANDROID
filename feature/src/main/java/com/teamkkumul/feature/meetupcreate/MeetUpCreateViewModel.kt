package com.teamkkumul.feature.meetupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.data.repository.MeetUpCreateLocationRepository
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MeetUpCreateLocationModel
import com.teamkkumul.model.MeetUpSealedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MeetUpCreateViewModel @Inject constructor(
    private val meetUpCreateLocationRepository: MeetUpCreateLocationRepository,
) : ViewModel() {

    private val _meetUpInputState = MutableStateFlow(false)
    val meetUpInputState: StateFlow<Boolean> get() = _meetUpInputState

    private val _meetUpLocation = MutableStateFlow<String>("")
    val meetUpLocation: StateFlow<String> get() = _meetUpLocation

    private val _meetUpDate = MutableStateFlow<String>("")
    val meetUpDate: StateFlow<String> get() = _meetUpDate

    private val _meetUpTime = MutableStateFlow<String>("")
    val meetUpTime: StateFlow<String> get() = _meetUpTime

    private val _meetUpCreateLocationState =
        MutableStateFlow<UiState<List<MeetUpCreateLocationModel.Location>>>(UiState.Loading)
    val meetUpCreateLocationState get() = _meetUpCreateLocationState.asStateFlow()

    private val _progressLiveData = MutableLiveData<Int>(0)
    val progressLiveData: LiveData<Int> get() = _progressLiveData

    private val _members = MutableLiveData<List<MeetUpSealedItem.Participant>>()
    val members: LiveData<List<MeetUpSealedItem.Participant>> get() = _members

    private val _meetUpName = MutableStateFlow<Boolean>(false)

    fun setMeetUpDate(input: String) {
        viewModelScope.launch {
            _meetUpDate.emit(input)
            validateForm()
        }
    }

    fun setMeetUpTime(input: String) {
        viewModelScope.launch {
            _meetUpTime.emit(input)
            validateForm()
        }
    }

    fun setProgressBar(value: Int) {
        _progressLiveData.value = value
    }

    fun setMeetUpLocation(location: String) {
        viewModelScope.launch {
            _meetUpLocation.emit(location)
            validateForm()
        }
    }

    fun setMeetUpName(input: Boolean) {
        viewModelScope.launch {
            _meetUpName.emit(input)
            validateForm()
        }
    }

    fun getMeetUpCreateLocation(q: String) = viewModelScope.launch {
        meetUpCreateLocationRepository.getMeetUpCreateLocation(q)
            .onSuccess { meetUpCreateLocationRepository ->
                _meetUpCreateLocationState.emit(UiState.Success(meetUpCreateLocationRepository))
            }.onFailure { exception ->
                _meetUpCreateLocationState.emit(UiState.Failure(exception.message.toString()))
            }
    }

    private fun validateForm() {
        viewModelScope.launch {
            val isFormValid =
                _meetUpLocation.value.isNotEmpty() &&
                    _meetUpDate.value.isNotEmpty() &&
                    _meetUpTime.value.isNotEmpty() &&
                    _meetUpName.value

            _meetUpInputState.emit(isFormValid)
        }
    }

    init {
    }

    init {
        val mockMembers = listOf(
            MeetUpSealedItem.Participant(
                id = 2,
                name = "Eric",
                profileImg = "https://example.com/alice.jpg",
            ),
            MeetUpSealedItem.Participant(
                id = 3,
                name = "Bob",
                profileImg = "https://example.com/alice.jpg",
            ),
            MeetUpSealedItem.Participant(
                id = 2,
                name = "Alice",
                profileImg = "https://example.com/alice.jpg",
            ),
            MeetUpSealedItem.Participant(
                id = 2,
                name = "Eric",
                profileImg = "https://example.com/alice.jpg",
            ),
            MeetUpSealedItem.Participant(
                id = 3,
                name = "Bob",
                profileImg = "https://example.com/alice.jpg",
            ),
            MeetUpSealedItem.Participant(
                id = 2,
                name = "Alice",
                profileImg = "https://example.com/alice.jpg",
            ),
        )
        _members.value = mockMembers
    }
}
