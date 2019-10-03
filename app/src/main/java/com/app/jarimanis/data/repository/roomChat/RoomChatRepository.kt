package com.app.jarimanis.data.repository.roomChat

import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.message.ReciveMessage
import com.app.jarimanis.data.datasource.models.message.Sender
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import retrofit2.Response

interface RoomChatRepository {

    fun deleteMessage()
    suspend fun sendMessage(sender: Sender): Response<ReciveMessage>
    fun receiveMessage(channelId: String): Query
}