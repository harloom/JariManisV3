package com.app.jarimanis.data.repository.commentar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.jarimanis.ui.thread.comentar.KomentarViewModel

class DiskusiModelFactory constructor(private  val categori : String, private val repo  : DiskusiRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KomentarViewModel::class.java)) {
            return KomentarViewModel(categori,repo) as T
        }

        throw IllegalArgumentException("ViewModel class does not exist")
    }
}