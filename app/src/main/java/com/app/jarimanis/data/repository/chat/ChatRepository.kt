package com.app.jarimanis.data.repository.chat

import androidx.lifecycle.LiveData

import com.app.jarimanis.data.datasource.models.chats.Chats
import com.app.jarimanis.data.datasource.models.chats.Result
import com.app.jarimanis.data.datasource.models.thread.Threads
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import retrofit2.Response

interface ChatRepository {
        fun getChatChannels() : LiveData<List<Result?>>?
        fun cancelJobs()
}