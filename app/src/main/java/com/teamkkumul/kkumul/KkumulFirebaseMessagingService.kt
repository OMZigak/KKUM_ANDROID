package com.teamkkumul.kkumul

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.teamkkumul.feature.MainActivity
import com.teamkkumul.kkumul.KkumulFirebaseMessagingService.FcmTag.CHANNEL_ID
import com.teamkkumul.kkumul.KkumulFirebaseMessagingService.FcmTag.NOTIFICATION_BODY
import com.teamkkumul.kkumul.KkumulFirebaseMessagingService.FcmTag.NOTIFICATION_ID
import com.teamkkumul.kkumul.KkumulFirebaseMessagingService.FcmTag.NOTIFICATION_TITLE
import com.teamkkumul.kkumul.KkumulFirebaseMessagingService.FcmTag.RELATED_CONTENT_ID
import timber.log.Timber

class KkumulFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var title: String
    private lateinit var body: String
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("fcm new token : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        sendPushAlarm(
            title = if (::title.isInitialized) title else "",
            body = if (::body.isInitialized) body else "",
            contentId = message.data[RELATED_CONTENT_ID] ?: "-1",
        )
    }

    private fun sendPushAlarm(title: String, body: String, contentId: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val notification = buildNotification(title, body, contentId)
        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    override fun handleIntent(intent: Intent?) {
        val newPushAlarmIntent = intent?.apply {
            val temp = extras?.apply {
                title = getString(NOTIFICATION_TITLE).orEmpty()
                body = getString(NOTIFICATION_BODY).orEmpty()
                remove(Constants.MessageNotificationKeys.ENABLE_NOTIFICATION)
                remove(getKeyWithOldPrefix())
            }
            replaceExtras(temp)
        }
        super.handleIntent(newPushAlarmIntent)
    }

    private fun getKeyWithOldPrefix(): String {
        val key = Constants.MessageNotificationKeys.ENABLE_NOTIFICATION
        return if (!key.startsWith(Constants.MessageNotificationKeys.NOTIFICATION_PREFIX)) {
            key
        } else {
            key.replace(
                Constants.MessageNotificationKeys.NOTIFICATION_PREFIX,
                Constants.MessageNotificationKeys.NOTIFICATION_PREFIX_OLD,
            )
        }
    }

    private fun buildNotification(
        title: String,
        body: String,
        contentId: String,
    ): Notification {
        val pendingIntent = createPendingIntent(contentId)
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(com.teamkkumul.feature.R.drawable.img_kum_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setColor(ContextCompat.getColor(this, com.teamkkumul.feature.R.color.main_color))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setShowWhen(true)
            .build()
    }

    private fun createPendingIntent(contentId: String): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(RELATED_CONTENT_ID, contentId)
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE,
        )
    }

    object FcmTag {
        const val RELATED_CONTENT_ID = "relateContentId"
        const val CHANNEL_NAME = "FCM_CHANNEL"
        const val CHANNEL_ID = "FCM_CHANNEL_ID"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_TITLE = "gcm.notification.title"
        const val NOTIFICATION_BODY = "gcm.notification.body"
    }
}
