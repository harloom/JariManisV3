package com.app.jarimanis.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.jarimanis.data.datasource.models.chats.Result
import com.app.jarimanis.data.repository.chat.ChatRepository

class ChatViewModel constructor(private val repo : ChatRepository) : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is Chat Fragment"
//    }
//    val text: LiveData<String> = _text

    val myChatChannels  : LiveData<List<Result?>>? = repo.getChatChannels()
}
