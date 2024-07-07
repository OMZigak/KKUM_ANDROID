package com.teamkkumul.core.data.repository

import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    fun getAccessToken(): Flow<String>
    fun getRefreshToken(): Flow<String>
    fun getAutoLogin(): Flow<Boolean>
    fun getMemberId(): Flow<Int>

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun saveAutoLogin(autoLogin: Boolean)

    suspend fun saveMemberId(id: Int)

    suspend fun clearAll()

    suspend fun clearForRefreshToken()
}
