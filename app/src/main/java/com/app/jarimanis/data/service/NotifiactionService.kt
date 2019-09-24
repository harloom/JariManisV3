package com.app.jarimanis.data.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotifiactionService : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    private fun setToken(token : String){

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.data.isNotEmpty().let{

        }


        message.notification?.let {

        }
    }


    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}