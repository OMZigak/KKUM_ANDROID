package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.repository.UserInfoRepository
import com.teamkkumul.core.datastore.datasource.KumulPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserInfoRepositoryImpl @Inject constructor(
    private val kumulPreferencesDataSource: KumulPreferencesDataSource,
) : UserInfoRepository {
    override fun getAccessToken(): Flow<String> =
        kumulPreferencesDataSource.accessToken

    override fun getRefreshToken(): Flow<String> =
        kumulPreferencesDataSource.refreshToken

    override fun getAutoLogin(): Flow<Boolean> =
        kumulPreferencesDataSource.autoLogin

    override fun getMemberName(): Flow<String> =
        kumulPreferencesDataSource.memberName

    override suspend fun saveAccessToken(accessToken: String) {
        kumulPreferencesDataSource.updateAccessToken(accessToken)
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        kumulPreferencesDataSource.updateRefreshToken(refreshToken)
    }

    override suspend fun saveAutoLogin(autoLogin: Boolean) {
        kumulPreferencesDataSource.updateAutoLogin(autoLogin)
    }

    override suspend fun saveMemberName(name: String) {
        kumulPreferencesDataSource.updateMemberName(name)
    }

    override suspend fun clearAll() {
        kumulPreferencesDataSource.clear()
    }

    override suspend fun clearForRefreshToken() {
        kumulPreferencesDataSource.clearForRefreshToken()
    }
}
