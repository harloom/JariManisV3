package com.app.jarimanis.data.repository.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.jarimanis.ui.thread.ThreadListViewModel

class ThreadModelFactory constructor(private  val categori : String,private val repo  : ThreadRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThreadListViewModel::class.java)) {
            return ThreadListViewModel(categori,repo) as T
        }

        throw IllegalArgumentException("ViewModel class does not exist")
    }
}