package com.app.jarimanis.data.repository.roomChat

import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.message.ReciveMessage
import com.app.jarimanis.data.datasource.models.message.Sender
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query

interface RoomChatRepository {

    fun deleteMessage()
    fun sendMessage(sender: Sender)
    fun receiveMessage(channelId: String): Query
}