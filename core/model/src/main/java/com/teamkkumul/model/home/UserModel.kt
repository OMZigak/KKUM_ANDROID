package com.teamkkumul.model.home

data class UserModel(
    val userId: Int,
    val level: Int,
    val name: String?,
    val profileImg: String?,
    val promiseCount: Int,
    val tardyCount: Int,
    val tardySum: Int,
)
