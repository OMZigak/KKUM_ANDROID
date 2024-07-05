package com.teamkkumul.core.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DefaultKumulPreferenceDatasource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : KumulPreferencesDataSource {
    private object PreferencesKeys {
        val AccessToken = stringPreferencesKey("accessToken")
        val RefreshToken = stringPreferencesKey("refreshToken")
        val Nickname = stringPreferencesKey("nickname")
        val Id = intPreferencesKey("id")
        val AutoLogin = booleanPreferencesKey("autoLogin")
    }

    override val accessToken: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.AccessToken].orEmpty()
        }

    override val refreshToken: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[PreferencesKeys.RefreshToken].orEmpty()
        }

    override val autoLogin: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.AutoLogin] ?: false
        }

    override val memberId: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[PreferencesKeys.Id]?.toInt() ?: -1
        }

    override suspend fun updateAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AccessToken] = accessToken
        }
    }

    override suspend fun updateRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.RefreshToken] = refreshToken
        }
    }

    override suspend fun updateAutoLogin(autoLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AutoLogin] = autoLogin
        }
    }

    override suspend fun updateMemberId(id: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.Id] = id
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun clearForRefreshToken() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.RefreshToken)
        }
    }
}
