package com.app.jarimanis.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService

object NotificationID {
        const val N_IDUPLOAD= 10



        const  val CHANNEL_NAME1: String = "Channel Notif"
        const val CHANNEL_ID1: String = "JariManis C1"
        const val CHANNEL_DESC1 : String = "Pemberitahuan"


        const val CHANNEL_NAME2: String = "Channel Message"
        const val CHANNEL_ID2: String = "JariManis C2"
        const val CHANNEL_DESC2 : String = "Pemberitahuan Pesan"

        const val CHANNEL_NAMEUPLOAD: String = "Channel Upload"
        const val CHANNEL_IDUPLOAD: String = "JariManis C3"
        const val CHANNEL_DESCUPLOAD : String = "Pemberitahuan Uploaded"

        val GROUP_MESSAGE = "com.android.jarimanis.message"

        fun createChannel(context: Context){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val importance = NotificationManager.IMPORTANCE_HIGH
                        val channel_notif = NotificationChannel(CHANNEL_ID1, CHANNEL_NAME1, importance).apply {
                                description = CHANNEL_DESC1
                        }
                        // Register the channel with the system
                        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        val channel_message = NotificationChannel(CHANNEL_ID2, CHANNEL_NAME2, importance).apply {
                                description = CHANNEL_DESC2
                        }

                        val channel_upload = NotificationChannel(CHANNEL_IDUPLOAD, CHANNEL_NAMEUPLOAD, importance).apply {
                                description = CHANNEL_DESCUPLOAD
                        }



                        notificationManager.createNotificationChannel(channel_notif)
                        notificationManager.createNotificationChannel(channel_message)
                        notificationManager.createNotificationChannel(channel_upload)

                }
        }



}