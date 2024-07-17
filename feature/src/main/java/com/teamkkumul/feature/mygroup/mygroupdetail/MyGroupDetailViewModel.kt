package com.teamkkumul.feature.mygroup.mygroupdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.core.ui.view.UiState
import com.teamkkumul.model.MyGroupMeetUpModel
import com.teamkkumul.model.MyGroupDetailSealedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGroupDetailViewModel @Inject constructor() : ViewModel() {
    private val _promise: MutableLiveData<UiState<List<MyGroupMeetUpModel.Promise>>> =
        MutableLiveData(UiState.Loading)
    val promise: LiveData<UiState<List<MyGroupMeetUpModel.Promise>>> get() = _promise

    private val _members = MutableLiveData<List<MyGroupDetailSealedItem.Member>>()
    val members: LiveData<List<MyGroupDetailSealedItem.Member>> get() = _members

    fun getPromise() {
        viewModelScope.launch {
            _promise.value = UiState.Empty
        }
    }

//    init {
//        getPromise()
//    }

    init {
        val mockMembers = listOf(
            MyGroupDetailSealedItem.Member(id = 3, "Alice", "https://example.com/alice.jpg"),
            MyGroupDetailSealedItem.Member(2, "Bob", "https://example.com/bob.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupDetailSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
        )
        _members.value = mockMembers
    }

    init {
        val mockPromise = listOf(
            MyGroupMeetUpModel.Promise(
                3,
                date = "2024.07.30",
                name = "약속명",
                placeName = "홍대입구",
                time = "PM 6:00",
            ),
            MyGroupMeetUpModel.Promise(
                3,
                date = "2024.07.30",
                name = "약속명",
                placeName = "홍대입구",
                time = "PM 6:00",
            ),
            MyGroupMeetUpModel.Promise(
                3,
                date = "2024.07.30",
                name = "약속명",
                placeName = "홍대입구",
                time = "PM 6:00",
            ),
        )
        _promise.value = UiState.Success(mockPromise)
    }
}
