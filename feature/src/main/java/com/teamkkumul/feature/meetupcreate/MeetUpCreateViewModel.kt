package com.teamkkumul.feature.meetupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MeetUpCreateLocationModel
import com.teamkkumul.model.MeetUpSealedItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MeetUpCreateViewModel @Inject constructor() : ViewModel() {
    private val _meetUpLocation = MutableLiveData<String>()
    val meetUpLocation: LiveData<String> get() = _meetUpLocation

    private val _meetUpDate = MutableStateFlow<String>("")
    val meetUpDate: StateFlow<String> get() = _meetUpDate

    private val _meetUpTime = MutableStateFlow<String>("")
    val meetUpTime: StateFlow<String> get() = _meetUpTime

    private val _location: MutableLiveData<UiState<List<MeetUpCreateLocationModel.Place>>> =
        MutableLiveData(UiState.Loading)
    val location: LiveData<UiState<List<MeetUpCreateLocationModel.Place>>> get() = _location

    private val _progressLiveData = MutableLiveData<Int>(0)
    val progressLiveData: LiveData<Int> get() = _progressLiveData

    private val _members = MutableLiveData<List<MeetUpSealedItem.Participant>>()
    val members: LiveData<List<MeetUpSealedItem.Participant>> get() = _members

    fun setMeetUpDate(input: String) {
        _meetUpDate.value = input
    }

    fun setMeetUpTime(input: String) {
        _meetUpTime.value = input
    }

    fun setProgressBar(value: Int) {
        _progressLiveData.value = value
    }

    fun setMeetUpLocation(location: String) {
        _meetUpLocation.value = location
    }

    fun getLocation() {
        viewModelScope.launch {
            _location.value = UiState.Empty
        }
    }

    init {
        val mockLocation = listOf(
            MeetUpCreateLocationModel.Place(
                "서울 중구 필동3가 26-1",
                "동국대학교 서울캠퍼스",
                "서울 중구 필동로1길 30",
            ),
            MeetUpCreateLocationModel.Place(
                "서울 중구 필동3가 26-1",
                "숙명여대",
                "서울 중구 필동로1길 30",
            ),
            MeetUpCreateLocationModel.Place(
                "서울 중구 필동3가 26-1",
                "동국대학교 서울캠퍼스",
                "서울 중구 필동로1길 30",
            ),
            MeetUpCreateLocationModel.Place(
                "서울 중구 필동3가 26-1",
                "동국대학교 서울캠퍼스",
                "서울 중구 필동로1길 30",
            ),
            MeetUpCreateLocationModel.Place(
                "서울 중구 필동3가 26-1",
                "동국대학교 서울캠퍼스",
                "서울 중구 필동로1길 30",
            ),
            MeetUpCreateLocationModel.Place(
                "서울 중구 필동3가 26-1",
                "동국대학교 서울캠퍼스",
                "서울 중구 필동로1길 30",
            ),
            MeetUpCreateLocationModel.Place(
                "서울 중구 필동3가 26-1",
                "동국대학교 서울캠퍼스",
                "서울 중구 필동로1길 30",
            ),

        )
        _location.value = UiState.Success(mockLocation)
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
