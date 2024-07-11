package com.teamkkumul.feature.meetup.meetupdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamkkumul.model.MeetUpSealedItem

class MeetUpDetailFriendViewModel : ViewModel() {
    private val _members = MutableLiveData<List<MeetUpSealedItem.Participant>>()
    val members: LiveData<List<MeetUpSealedItem.Participant>> get() = _members

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
