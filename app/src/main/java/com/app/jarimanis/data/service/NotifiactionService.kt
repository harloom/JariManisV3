package com.app.jarimanis.data.service

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.api.UserAPI
import com.app.jarimanis.data.datasource.local.MenuData
import com.app.jarimanis.data.datasource.models.token.FirebaseToken
import com.app.jarimanis.utils.NotificationID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import kotlin.random.Random

class NotifiactionService : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        setToken(p0)
    }

    private fun setToken(token : String){
        val api : UserAPI = get()
        try {
            CoroutineScope(IO).launch {
                val result =   api.putTokenNotification(FirebaseToken(token))
                if(result.isSuccessful){
                    println("OK Token Sent")
                }
            }
        }catch (e : Exception){
            println("Debug : ${e.toString()}")

        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.data.isNotEmpty().let{


    }
     message.notification?.let {
        Log.d("MSG", "Message Notification Body: ${it.body} ${it.channelId}")
         displayNotification(it)
    }
}

    private fun displayNotification(it : RemoteMessage.Notification) {
        MenuData.chatExits = true

        val manager =
            applicationContext.getSystemService(    Context.NOTIFICATION_SERVICE) as NotificationManager

       val notif = NotificationCompat.Builder(
           applicationContext,NotificationID.CHANNEL_ID2
       )   .setContentTitle(it.title)
           .setContentText(it.body)
           .setSmallIcon(R.mipmap.ic_launcher)
           .setGroup(NotificationID.GROUP_MESSAGE).build()


        val notifSumary = NotificationCompat.Builder(
            applicationContext,NotificationID.CHANNEL_ID2

        )
            .setContentTitle("Pemberitahuan")
            .setContentText("Silahkan Di cek")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setGroup(NotificationID.GROUP_MESSAGE)
            .setStyle(NotificationCompat.InboxStyle()
                .addLine(it.body)
                .setSummaryText("Pesan Baru"))
            .setGroupSummary(true)
            .build()



             manager.apply {
                 notify((100 until 1150).random(),notif)
                 notify(200,notifSumary)
             }
    }
    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}