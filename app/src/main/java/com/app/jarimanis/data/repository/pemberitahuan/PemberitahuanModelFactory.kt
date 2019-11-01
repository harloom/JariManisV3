package com.app.jarimanis.data.repository.pemberitahuan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.jarimanis.ui.notifications.PemberitahuanViewModel

class PemberitahuanModelFactory constructor( private val repo  : PemberitahuanRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PemberitahuanViewModel::class.java)) {
            return PemberitahuanViewModel(repo) as T
        }

        throw IllegalArgumentException("ViewModel class does not exist")
    }
}