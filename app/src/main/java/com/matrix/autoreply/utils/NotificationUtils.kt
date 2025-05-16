package com.matrix.autoreply.utils

import android.service.notification.StatusBarNotification
import android.os.Parcelable
import com.matrix.autoreply.model.NotificationWear
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import java.util.*

object NotificationUtils {
    private const val MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS = 2 * 60 * 1000
    fun getTitle(sbn: StatusBarNotification): String? {
        var title: String?
        if (sbn.notification.extras.getBoolean("android.isGroupConversation")) {
            title = sbn.notification.extras.getString("android.hiddenConversationTitle")

            if (title == null) {
                title = sbn.notification.extras.getString("android.title")
                val index = title!!.indexOf(':')
                if (index != -1) {
                    title = title.substring(0, index)
                }
            }

            val b = sbn.notification.extras["android.messages"] as Array<Parcelable>?
            if (b != null && b.size > 1) {
                val startIndex = title.lastIndexOf('(')
                if (startIndex != -1) {
                    title = title.substring(0, startIndex)
                }
            }
        } else {
            title = sbn.notification.extras.getString("android.title")
        }
        return title
    }


    fun isNewNotification(sbn: StatusBarNotification): Boolean {

        return sbn.notification.`when` == 0L ||
                System.currentTimeMillis() - sbn.notification.`when` < MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS
    }

    fun extractWearNotification(statusBarNotification: StatusBarNotification): NotificationWear {
        val wearableExtender = NotificationCompat.WearableExtender(statusBarNotification.notification)
        val actions = wearableExtender.actions
        val remoteInputs: MutableList<RemoteInput> = ArrayList(actions.size)
        var pendingIntent: PendingIntent? = null
        for (act in actions) {
            if (act != null && act.remoteInputs != null) {
                for (x in act.remoteInputs!!.indices) {
                    val remoteInput = act.remoteInputs!![x]
                    remoteInputs.add(remoteInput)
                    pendingIntent = act.actionIntent
                }
            }
        }
        return NotificationWear(
            statusBarNotification.packageName,
            pendingIntent,
            remoteInputs,
            statusBarNotification.notification.extras,
            statusBarNotification.tag,
            UUID.randomUUID().toString()
        )
    }

    fun getTitleRaw(sbn: StatusBarNotification): String? {
        return sbn.notification.extras.getString("android.title")
    }

    fun getMessage(sbn: StatusBarNotification): String? {
        if (!isValidMessage(sbn.notification.extras.getString("android.text")))
            return null
        return sbn.notification.extras.getString("android.text")
    }

    private fun isValidMessage(msg: String?): Boolean {
        return if (msg != null && msg.length > 2) {
            !msg.contains("This message was deleted") &&
                    !msg.contains("new messages") && msg != "\uD83D\uDCF7 Photo" &&
                    msg != "Calling…" && msg != "Ringing…" && msg != "Missed voice call" &&
                    msg != "Incoming voice call" && msg != "Ongoing video call" &&
                    !msg.contains("Sticker") && !msg.contains("missed calls") &&
                    msg != "\uD83D\uDCF9 Incoming video call" && msg.substring(2) != "GIF" &&
                    msg.substring(2) != "Video (" && !msg.contains("Sending video to") &&
                    !msg.contains("Sending file to") && !msg.contains("files to") &&
                    !msg.contains("videos to") && !msg.contains("Sending GIF to")
        } else true
    }
}