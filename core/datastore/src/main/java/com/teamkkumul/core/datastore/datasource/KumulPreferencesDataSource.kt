package com.teamkkumul.core.datastore.datasource

import kotlinx.coroutines.flow.Flow

interface KumulPreferencesDataSource {
    val accessToken: Flow<String>
    val refreshToken: Flow<String>
    val autoLogin: Flow<Boolean>
    val memberId: Flow<Int>

    suspend fun updateAccessToken(accessToken: String)
    suspend fun updateRefreshToken(refreshToken: String)

    suspend fun updateAutoLogin(autoLogin: Boolean)
    suspend fun updateMemberId(id: Int)

    suspend fun clear()
    suspend fun clearForRefreshToken()
}
