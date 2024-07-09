package com.teamkkumul.feature.mygroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MyGroupMeetUpModel
import kotlinx.coroutines.launch

class MyGroupMeetUpViewModel : ViewModel() {
    private val _promise: MutableLiveData<UiState<List<MyGroupMeetUpModel.Promise>>> =
        MutableLiveData(UiState.Loading)
    val promise: LiveData<UiState<List<MyGroupMeetUpModel.Promise>>> get() = _promise

    fun getPromise() {
        viewModelScope.launch {
            _promise.value = UiState.Empty
        }
    }

    init {
        getPromise()
    }

//    init {
//        val mockPromise = listOf(
//            MyGroupMeetUpModel.Promise(
//                name = "기경",
//                date = "2024",
//                placeName = "홍대",
//                dDay = 2,
//                time = "PM 2:00",
//            ),
//            MyGroupMeetUpModel.Promise(
//                name = "모각작",
//                date = "2024",
//                placeName = "용산",
//                dDay = 2,
//                time = "PM 8:00",
//            ),
//            MyGroupMeetUpModel.Promise(
//                name = "기경",
//                date = "2024",
//                placeName = "홍대",
//                dDay = 2,
//                time = "PM 2:00",
//            ),
//            MyGroupMeetUpModel.Promise(
//                name = "모각작",
//                date = "2024",
//                placeName = "용산",
//                dDay = 2,
//                time = "PM 8:00",
//            ),
//            MyGroupMeetUpModel.Promise(
//                name = "기경",
//                date = "2024",
//                placeName = "홍대",
//                dDay = 2,
//                time = "PM 2:00",
//            ),
//            MyGroupMeetUpModel.Promise(
//                name = "모각작",
//                date = "2024",
//                placeName = "용산",
//                dDay = 2,
//                time = "PM 8:00",
//            ),
//            MyGroupMeetUpModel.Promise(
//                name = "기경",
//                date = "2024",
//                placeName = "홍대",
//                dDay = 3,
//                time = "PM 2:00",
//            ),
//        )
//    }
}
