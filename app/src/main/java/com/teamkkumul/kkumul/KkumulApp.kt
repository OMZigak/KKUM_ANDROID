package com.teamkkumul.kkumul

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.teamkkumul.core.network.BuildConfig
import com.teamkkumul.kkumul.BuildConfig.KAKAO_APP_KEY
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class KkumulApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setTimber()
        setDarkMode()
        setKaKaoSdk()
    }

    private fun setTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setKaKaoSdk() {
        KakaoSdk.init(this, KAKAO_APP_KEY)
        // 요건 각자 찍어보고 저한테 알려주세요!
        val keyHash = Utility.getKeyHash(this)
        Timber.tag("Hash").d(keyHash)
    }
}
