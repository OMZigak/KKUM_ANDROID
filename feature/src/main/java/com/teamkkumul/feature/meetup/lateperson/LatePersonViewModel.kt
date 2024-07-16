package com.teamkkumul.feature.meetup.lateperson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamkkumul.model.LatePerson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LatePersonViewModel : ViewModel() {

    private val _latePerson = MutableStateFlow<List<LatePerson>>(emptyList())
    val latePerson: StateFlow<List<LatePerson>> = _latePerson

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        val dummyData = listOf(
            LatePerson(1, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(2, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(3, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(4, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(5, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(6, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(7, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(8, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(9, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(10, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(11, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(12, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(13, "혜진 티엘", "https://example.com/image1.jpg"),
        )
        viewModelScope.launch {
            _latePerson.emit(dummyData)
        }
    }
}
