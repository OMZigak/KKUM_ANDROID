package com.teamkkumul.feature.meetup.lateperson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.model.LatePersonModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LatePersonViewModel : ViewModel() {

    private val _latePerson = MutableStateFlow<List<LatePersonModel.LateComers>>(emptyList())
    val latePerson: StateFlow<List<LatePersonModel.LateComers>> = _latePerson

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        val dummyData = listOf(
            LatePersonModel.LateComers(1, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(2, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(3, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(4, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(5, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(6, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(7, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(8, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(9, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(10, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(11, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(12, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePersonModel.LateComers(13, "혜진 티엘", "https://example.com/image1.jpg"),
        )
        viewModelScope.launch {
            _latePerson.emit(dummyData)
        }
    }
}
