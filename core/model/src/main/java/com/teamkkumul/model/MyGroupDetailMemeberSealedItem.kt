package com.teamkkumul.model

sealed class MyGroupDetailMemeberSealedItem {
    data class MyGroupDetailMemeberPlus(
        val num: Int,
    ) : MyGroupDetailMemeberSealedItem()

    data class Member(
        val memberId: Int,
        val name: String,
        val profileImg: String?,
    ) : MyGroupDetailMemeberSealedItem()
}
