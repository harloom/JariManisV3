package com.app.jarimanis.data.repository.thread.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.ui.dashboard.ThreadMeListViewModel

class ThreaduserModelFactory constructor(private  val uid : String, private val repo  : ThreadRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThreadMeListViewModel::class.java)) {
            return ThreadMeListViewModel(uid,repo) as T
        }

        throw IllegalArgumentException("ViewModel class does not exist")
    }
}