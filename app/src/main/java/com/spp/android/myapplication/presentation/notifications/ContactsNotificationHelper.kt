package com.spp.android.myapplication.presentation.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.spp.android.myapplication.R

object ContactsNotificationHelper {

    private const val CHANNEL_ID = "contacts_changes"
    private const val CHANNEL_NAME = "Contacts changes"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (nm.getNotificationChannel(CHANNEL_ID) != null) return

        nm.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        )
    }

    private fun detailPendingIntent(context: Context, contactId: Int): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("myapp://contact/$contactId")).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            contactId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun showDeleted(context: Context, contactId: Int) {
        ensureChannel(context)
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = context.getString(R.string.deleted_contact_toast_text)
        val text  = context.getString(R.string.notification_text)

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.outline_arrows_output_24).setContentTitle(title)
            .setContentText(text).setAutoCancel(true)
            .setContentIntent(detailPendingIntent(context, contactId)).build()

        nm.notify(10_000 + contactId, notif)
    }

    fun showAdded(context: Context, contactId: Int) {
        ensureChannel(context)
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = context.getString(R.string.contact_added)
        val text  = context.getString(R.string.notification_text)

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.outline_arrows_input_24).setContentTitle(title)
            .setContentText(text).setAutoCancel(true)
            .setContentIntent(detailPendingIntent(context, contactId)).build()

        nm.notify(20_000 + contactId, notif)
    }
}