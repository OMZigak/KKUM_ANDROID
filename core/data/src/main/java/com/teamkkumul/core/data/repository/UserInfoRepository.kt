package com.teamkkumul.core.data.repository

import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    fun getAccessToken(): Flow<String>
    fun getRefreshToken(): Flow<String>
    fun getAutoLogin(): Flow<Boolean>
    fun getMemberName(): Flow<String>

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun saveAutoLogin(autoLogin: Boolean)

    suspend fun saveMemberName(name: String)

    suspend fun clearAll()

    suspend fun clearForRefreshToken()
}
