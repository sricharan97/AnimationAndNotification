package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

// Notification ID.
private val NOTIFICATION_ID = 0

/**
 * Extension function to send notification
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 * @param messageBody, message to be displayed in the notification
 */

fun NotificationManager.sendNotification(fileName: String, status: String, applicationContext: Context) {

    // Create the content intent for the notification, which launches
    // this activity
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("fileName", fileName)
    contentIntent.putExtra("status", status)


    //Create a Pending Intent
    val pendingIntent = PendingIntent.getActivity(applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)


    //get an instance of NotificationCompat.Builder
    //Build the notification
    val builder = NotificationCompat.Builder(applicationContext,
            applicationContext.getString(R.string.download_channel_id))
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_description))
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_assistant_black_24dp, applicationContext.getString(R.string.notification_button),
                    pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())

}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}