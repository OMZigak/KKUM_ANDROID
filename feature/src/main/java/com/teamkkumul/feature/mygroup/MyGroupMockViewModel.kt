package com.teamkkumul.feature.mygroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamkkumul.model.MyGroupSealedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyGroupMockViewModel @Inject constructor() : ViewModel() {

    private val _members = MutableLiveData<List<MyGroupSealedItem.Member>>()
    val members: LiveData<List<MyGroupSealedItem.Member>> get() = _members

    init {
        val mockMembers = listOf(
            MyGroupSealedItem.Member(id = 3, "Alice", "https://example.com/alice.jpg"),
            MyGroupSealedItem.Member(2, "Bob", "https://example.com/bob.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
            MyGroupSealedItem.Member(3, "Charlie", "https://example.com/charlie.jpg"),
        )
        _members.value = mockMembers
    }
}
