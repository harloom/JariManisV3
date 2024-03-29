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
import com.app.jarimanis.utils.SendStatus.PENDING
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class RoomViewModel(private val roomChatRepository: RoomChatRepository) : ViewModel() {


    private val _from : MutableLiveData<Boolean> = MutableLiveData()
    val etMassage : LiveData<Boolean> = _from
    val respon : MutableLiveData<StatusMessage> = MutableLiveData<StatusMessage>()
    private val reciveMessage =  mutableListOf<ReciveMessage>()
     val data  = MutableLiveData<MutableList<ReciveMessage>>()
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

                    val rm = ReciveMessage(UUID.randomUUID().toString(),"","",massage.message,TokenUser.idUser,
                        "",
                        Timestamp.now(),
                        PENDING
                    )
                    reciveMessage.add(rm)
                    data.postValue(reciveMessage)

                    val res =
                        roomChatRepository.sendMessageAndCreateChannel(
                            SentNewChannel(message = massage.message, userList = listUser ,status = PENDING
                              ,image = "",video = ""))
                    if(res.isSuccessful){
                        withContext(Main){
                            val data = res.body()
                            println("Data : $data")
                            responChannel.value = StatusChannel(true, data?.channelId )
                            respon.value = StatusMessage(status = true)
                            fillterFindAndRemove(rm)
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
            val sender  = SentNewChannel("",listUser,"","",0)
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
                val rm = ReciveMessage(UUID.randomUUID().toString(),"","",massage.message,TokenUser.idUser,
                    "",
                    Timestamp.now(),
                    PENDING
                )
                reciveMessage.add(rm)
                data.postValue(reciveMessage)
               val res =  roomChatRepository.sendMessage(massage)
                withContext(Main) {
                    if (res.isSuccessful) {
                        respon.value = StatusMessage(status = true)
                        fillterFindAndRemove(rm)
                    } else {
                        respon.value = StatusMessage(status = false)

                    }
                }

            }

        }catch (e : Exception){

        }

    }

    private fun fillterFindAndRemove(rcv : ReciveMessage){
        reciveMessage.withIndex().filter {
            it.value.id == rcv.id
        }.map {
            reciveMessage.removeAt(it.index)
            data.postValue(reciveMessage)
        }

    }


    fun getMessageRecive(channelId : String) {
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

    }

    private fun isEmpty(string: String): Boolean {
        return string.isNotEmpty()
    }
}
