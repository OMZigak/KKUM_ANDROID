package com.teamkkumul.model

sealed class MyGroupDetailMemeberSealedItem {
    data object MyGroupDetailMemeberPlus : MyGroupDetailMemeberSealedItem()

    data class Member(
        val memberId: Int,
        val name: String,
        val profileImg: String?,
    ) : MyGroupDetailMemeberSealedItem()
}
