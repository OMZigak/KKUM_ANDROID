package com.teamkkumul.feature.meetup.readystatus.readyinfoinput.alarm

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.teamkkumul.feature.MainActivity
import com.teamkkumul.feature.R
import com.teamkkumul.feature.utils.KeyStorage
import timber.log.Timber

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val tabIndex = intent?.getIntExtra(KeyStorage.TAB_INDEX, 0) ?: 0

        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.fragment_meet_up_container)
            .setArguments(
                Bundle().apply {
                    putInt(KeyStorage.TAB_INDEX, tabIndex)
                },
            )
            .createPendingIntent()

        val notificationTitle = intent?.getStringExtra(KeyStorage.ALARM_TITLE) ?: "알람 제목"
        val notificationContent = intent?.getStringExtra(KeyStorage.ALARM_CONTENT) ?: "알람 내용"

        val notification = NotificationCompat.Builder(context, KeyStorage.LOCAL_ALARM_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Timber.e("Notification permission not granted")
                return
            }
            notify(1, notification)
        }
    }
}
