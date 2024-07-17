package com.teamkkumul.kkumul

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import com.teamkkumul.core.network.BuildConfig
import com.teamkkumul.feature.utils.KeyStorage.LOCAL_ALARM_CHANNEL
import com.teamkkumul.kkumul.BuildConfig.KAKAO_APP_KEY
import com.teamkkumul.kkumul.KkumulFirebaseMessagingService.FcmTag.CHANNEL_ID
import com.teamkkumul.kkumul.KkumulFirebaseMessagingService.FcmTag.CHANNEL_NAME
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class KkumulApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setTimber()
        setDarkMode()
        setKaKaoSdk()
        createLocalNotificationChannel()
        createFcmNotificationChannel()
    }

    private fun setTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setKaKaoSdk() {
        KakaoSdk.init(this, KAKAO_APP_KEY)
    }

    private fun createLocalNotificationChannel() {
        val name = "로컬 알람 채널"
        val descriptionText = "로컬 알람을 위한 채널입니다."
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(LOCAL_ALARM_CHANNEL, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createFcmNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH,
        )
        notificationManager?.createNotificationChannel(channel)
    }
}
