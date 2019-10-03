package com.app.jarimanis.data.repository.roomChat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.jarimanis.data.datasource.api.JariManisAPI
import com.app.jarimanis.data.datasource.models.ResponseRegister
import com.app.jarimanis.data.datasource.models.UserRegister
import com.app.jarimanis.data.datasource.api.UserAPI
import com.app.jarimanis.data.datasource.models.chats.Chats
import com.app.jarimanis.data.datasource.models.chats.Result
import com.app.jarimanis.data.datasource.models.message.ReciveMessage
import com.app.jarimanis.data.datasource.models.message.Sender
import com.app.jarimanis.data.repository.chat.ChatRepository
import com.app.jarimanis.data.repository.firebase.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.core.OrderBy
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Response

class RoomChatRepositoryImp : RoomChatRepository {
    override fun sendMessage(sender : Sender) {

    }
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun receiveMessage(channelId  : String): Query {
       return db.collection("channels/$channelId/messages/").orderBy("timestamp", Query.Direction.ASCENDING)

    }

    override fun deleteMessage() {

    }

    var job : CompletableJob? = null



}

