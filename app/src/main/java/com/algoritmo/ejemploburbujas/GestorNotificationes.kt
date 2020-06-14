package com.algoritmo.ejemploburbujas

import android.app.*
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import java.lang.System.currentTimeMillis

class GestorNotificationes(private val context: Context) {
    companion object {
        private const val CHANNELS = "Algoritmo"
        private const val REQUEST_CONTENT = 1
        private const val REQUEST_BUBBLE = 2
        private const val NOTIFICATION_ID = 0
    }
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    init { setUpNotificationChannels() }
    private fun setUpNotificationChannels() {
        if (notificationManager?.getNotificationChannel(CHANNELS) == null) {
            val channel = NotificationChannel(
                CHANNELS,
                "Algoritmo Youtube",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "El mejor canal para aprender Android de la historia"
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
    private fun crearPersona(icon: Icon): Person {
        return Person.Builder()
            .setName("Algoritmo")
            .setIcon(icon)
            .setBot(true)
            .setImportant(true)
            .build()
    }
    private fun crearIntent(requestCode: Int): PendingIntent {
        return PendingIntent.getActivity(
            context,
            requestCode,
            Intent(context, NuevoVideoActivity::class.java),
            FLAG_UPDATE_CURRENT
        )
    }
    private fun crearBubbleMetadata(icon: Icon): Notification.BubbleMetadata {
        return Notification.BubbleMetadata.Builder(crearIntent(REQUEST_BUBBLE), icon)
            .setDesiredHeight(400)
            .setAutoExpandBubble(false)
            .setSuppressNotification(true)
            .build()
    }
    private fun crearNotification(mensaje: String, icon: Icon, person: Person): Notification.Builder {
        return Notification.Builder(context, CHANNELS)
            .setContentTitle(mensaje)
            .setLargeIcon(icon)
            .setSmallIcon(icon)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setStyle(
                Notification.MessagingStyle(person)
                    .setGroupConversation(false)
                    .addMessage(mensaje, currentTimeMillis(), person)
            )
            .addPerson(person)
            .setShowWhen(true)
            .setContentIntent(crearIntent(REQUEST_CONTENT))
    }
    fun mostrarNotification(mensaje: String) {
        val icon = Icon.createWithResource(context, R.drawable.logo)
        val person = crearPersona(icon)
        val notification = crearNotification(mensaje, icon, person)
        val bubbleMetaData = crearBubbleMetadata(icon)
        notification.setBubbleMetadata(bubbleMetaData)
        notificationManager?.notify(NOTIFICATION_ID, notification.build())
    }
}