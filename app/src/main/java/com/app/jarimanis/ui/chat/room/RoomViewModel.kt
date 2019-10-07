package com.app.jarimanis.ui.chat.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.message.ReciveMessage
import com.app.jarimanis.data.datasource.models.message.Sender
import com.app.jarimanis.data.datasource.models.message.SentNewChannel
import com.app.jarimanis.data.datasource.models.message.User
import com.app.jarimanis.data.repository.roomChat.RoomChatRepository
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RoomViewModel(private val roomChatRepository: RoomChatRepository) : ViewModel() {
    private val _from : MutableLiveData<Boolean> = MutableLiveData()
    val etMassage : LiveData<Boolean> = _from
    val respon : MutableLiveData<StatusMessage> = MutableLiveData<StatusMessage>()


    val responChannel : MutableLiveData<StatusChannel>  = MutableLiveData()
    val getChannel : LiveData<StatusChannel> = responChannel

    val getRespon : LiveData<StatusMessage> = respon
    fun fromMassageChange(message : String?) {
        message?.let {
            _from.value = isEmpty(it)
        }
    }

    fun sentNewChannel(massage: Sender, to: String?){
            try {
                CoroutineScope(IO).launch {
                    val listUser = mutableListOf<User>()
                    val TO  = User(to)
                    val FROM = User(TokenUser.idUser)
                    listUser.add(TO)
                    listUser.add(FROM)
                    val res =
                        roomChatRepository.sendMessageAndCreateChannel(SentNewChannel(message = massage.message,userList = listUser))
                    if(res.isSuccessful){
                        withContext(Main){
                            respon.value = StatusMessage(status = true)
                        }
                    }else{
                        withContext(Main){
                            respon.value = StatusMessage(status = false)
                        }
                    }

                }
            }catch (e : Exception){

            }
    }

    fun cekChannelExits(to : String){
        try {
            val listUser = mutableListOf<User>()
            val TO  = User(to)
            val FROM = User(TokenUser.idUser)
            listUser.add(TO)
            listUser.add(FROM)
            val sender  = SentNewChannel("",listUser)
           CoroutineScope(IO).launch {
               val res = roomChatRepository.cekChannelIsExits(sender)
               if(res.code() == 410){
                   withContext(Main){
                       responChannel.value = StatusChannel(false, null)
                   }
               }else if(res.code() == 200){
                   withContext(Main){
                        val data = res.body()
                        responChannel.value = StatusChannel(true, data?.channelId )
                   }
               }
           }
        }catch (e : Exception){

        }
    }

    fun sentMessage(massage : Sender){
        try {
            CoroutineScope(IO).launch {
               val res =  roomChatRepository.sendMessage(massage)
                if(res.isSuccessful){
                    withContext(Main){
                        respon.value = StatusMessage(status = true)
                    }
                }else{
                    withContext(Main){
                        respon.value = StatusMessage(status = false)
                    }
                }

            }

        }catch (e : Exception){

        }

    }


    fun getMessageRecive(channelId : String): LiveData<MutableList<ReciveMessage>> {
        val data  = MutableLiveData<MutableList<ReciveMessage>>()
        val reciveMessage =  mutableListOf<ReciveMessage>()
        roomChatRepository.receiveMessage(channelId).addSnapshotListener { snapshot, exception ->
            if(exception != null){
                return@addSnapshotListener
            }

            for (dc in snapshot!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        println("New Data Message : ${dc.document.data}")
                        val item = dc.document.toObject(ReciveMessage::class.java)
                        reciveMessage.add(item)
                    }
                    DocumentChange.Type.MODIFIED -> {

                    }
                    DocumentChange.Type.REMOVED ->{
                        reciveMessage.removeAt(dc.oldIndex)
                    }
                }
            }
            data.postValue(reciveMessage)
        }
        return data
    }

    private fun isEmpty(string: String): Boolean {
        return string.isNotEmpty()
    }
}
