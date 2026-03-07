package com.example.catlendar.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.catlendar.R

object NotificationHelper {

    private const val CHANNEL_ID = "CAT_FACT_CHANNEL"
    private const val CHANNEL_NAME = "Daily Cat Facts"
    private const val NOTIFICATION_ID = 1001

    fun showCatFactNotification(context: Context, fact: String) {
        createNotificationChannel(context)

        // Android 13+ requires POST_NOTIFICATIONS permission.
        // Assuming user has granted it in MainActivity for this project scope.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar) // Reusing calendar icon as cat icon proxy
            .setContentTitle("Daily Cat Fact 🐾")
            .setContentText(fact)
            .setStyle(NotificationCompat.BigTextStyle().bigText(fact))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Shows a daily random cat fact"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
