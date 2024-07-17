package com.teamkkumul.model

sealed class MyGroupDetailSealedItem {
    data class MyGroupDetailPlus(
        val num: Int,
    ) : MyGroupDetailSealedItem()

    data class Member(
        val id: Int,
        val name: String,
        val profileImg: String,
    ) : MyGroupDetailSealedItem()
}
