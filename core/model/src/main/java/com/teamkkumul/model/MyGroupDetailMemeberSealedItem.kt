package com.teamkkumul.model

sealed class MyGroupDetailMemeberSealedItem {
    data class MyGroupDetailMemeberPlus(
        val num: Int,
    ) : MyGroupDetailMemeberSealedItem()

    data class Member(
        val id: Int,
        val name: String,
        val profileImg: String?,
    ) : MyGroupDetailMemeberSealedItem()
}
