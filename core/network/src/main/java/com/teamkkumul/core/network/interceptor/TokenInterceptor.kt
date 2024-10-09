package com.teamkkumul.core.network.interceptor

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.teamkkumul.core.datastore.datasource.DefaultKumulPreferenceDatasource
import com.teamkkumul.core.network.api.LoginService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
    private val defaultKumulPreferenceDatasource: DefaultKumulPreferenceDatasource,
    private val context: Application,
    private val loginService: LoginService,
) : Interceptor {
    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var response = chain.proceed(originalRequest.newAuthBuilder())

        if (response.code == CODE_TOKEN_EXPIRE) {
            response.close()
            val tokenRefreshed = runBlocking {
                refreshTokenIfNeeded()
            }
            if (tokenRefreshed) {
                response = chain.proceed(originalRequest.newAuthBuilder())
            } else {
                handleFailedTokenReissue()
            }
        }
        return response
    }

    private suspend fun refreshTokenIfNeeded(): Boolean {
        mutex.withLock {
            val refreshToken = defaultKumulPreferenceDatasource.refreshToken.first()

            return try {
                val tokenResult = runBlocking(Dispatchers.IO) {
                    loginService.postReissueToken(refreshToken)
                }

                when (tokenResult.success) {
                    true -> {
                        tokenResult.data?.let {
                            defaultKumulPreferenceDatasource.updateAccessToken(it.accessToken)
                            defaultKumulPreferenceDatasource.updateRefreshToken(it.refreshToken)
                        }
                        true
                    }

                    false -> false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun handleFailedTokenReissue() = with(context) {
        CoroutineScope(Dispatchers.Main).launch {
            defaultKumulPreferenceDatasource.clear()
            startActivity(
                Intent.makeRestartActivityTask(
                    packageManager.getLaunchIntentForPackage(packageName)?.component,
                ),
            )
            Toast.makeText(context, "재 로그인이 필요해요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun Request.newAuthBuilder() = newBuilder()
        .addHeader(
            AUTHORIZATION,
            runBlocking {
                BEARER + defaultKumulPreferenceDatasource.accessToken.first()
            },
        ).build()

    companion object {
        const val CODE_TOKEN_EXPIRE = 401
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer "
    }
}
