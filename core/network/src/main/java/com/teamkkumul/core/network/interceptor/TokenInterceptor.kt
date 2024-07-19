package com.teamkkumul.core.network.interceptor

import android.app.Application
import android.content.Intent
import com.teamkkumul.core.datastore.datasource.DefaultKumulPreferenceDatasource
import com.teamkkumul.core.network.BuildConfig
import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseReissueTokenDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val json: Json,
    private val defaultKumulPreferenceDatasource: DefaultKumulPreferenceDatasource,
    private val context: Application,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authRequest = if (runBlocking { defaultKumulPreferenceDatasource.autoLogin.first() }) {
            originalRequest.newAuthBuilder()
        } else {
            originalRequest
        }
        val response = chain.proceed(authRequest)

        if (response.code == CODE_TOKEN_EXPIRE) {
            response.close()
            val refreshToken = runBlocking {
                defaultKumulPreferenceDatasource.refreshToken.first()
            }
            val refreshTokenRequest = originalRequest.newBuilder().get()
                .url("${BuildConfig.KKUMUL_BASE_URL}/api/v1/auth/reissue")
                .post("".toRequestBody("application/json".toMediaType()))
                .addHeader(AUTHORIZATION, refreshToken)
                .build()
            val refreshTokenResponse = chain.proceed(refreshTokenRequest)

            if (refreshTokenResponse.isSuccessful) {
                val responseRefresh =
                    json.decodeFromString<BaseResponse<ResponseReissueTokenDto>>(
                        refreshTokenResponse.body?.string()
                            ?: throw IllegalStateException("refreshTokenResponse is null $refreshTokenResponse"),
                    )

                refreshTokenResponse.close()
                runBlocking {
                    responseRefresh.data?.let {
                        defaultKumulPreferenceDatasource.updateAccessToken(
                            it.accessToken,
                        )
                        defaultKumulPreferenceDatasource.updateRefreshToken(
                            it.refreshToken,
                        )
                    }
                }

                val newRequest = originalRequest.newAuthBuilder()
                return chain.proceed(newRequest)
            } else {
                refreshTokenResponse.close()
                with(context) {
                    CoroutineScope(Dispatchers.Main).launch {
                        startActivity(
                            Intent.makeRestartActivityTask(
                                packageManager.getLaunchIntentForPackage(packageName)?.component,
                            ),
                        )
                        defaultKumulPreferenceDatasource.clear()
                    }
                }
            }
        }
        return response
    }

    private fun Request.newAuthBuilder() =
        this.newBuilder().addHeader(
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
