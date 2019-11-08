package com.app.jarimanis.data.repository.roomChat

import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.ChannelRespon
import com.app.jarimanis.data.datasource.models.message.ReciveMessage
import com.app.jarimanis.data.datasource.models.message.Sender
import com.app.jarimanis.data.datasource.models.message.SentNewChannel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import retrofit2.Response

interface RoomChatRepository {

    fun deleteMessage()
    suspend fun sendMessage(sender: Sender): Response<ReciveMessage>
    suspend fun cekChannelIsExits(sender: SentNewChannel): Response<ChannelRespon>
    suspend fun sendMessageAndCreateChannel(sender: SentNewChannel) : Response<ChannelRespon>
    fun receiveMessage(channelId: String): Query

    fun reciveMessage(channelId: String,last : DocumentSnapshot): Query
}