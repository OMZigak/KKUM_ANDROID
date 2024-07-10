package com.teamkkumul.model

sealed class MyGroupSealedItem {
    data class MyGroupPlus(
        val num: Int,
    ) : MyGroupSealedItem()

    data class Member(
        val id: Int,
        val name: String,
        val profileImg: String,
    ) : MyGroupSealedItem()
}
