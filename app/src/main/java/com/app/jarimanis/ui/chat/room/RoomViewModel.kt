package com.app.jarimanis.ui.chat.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class RoomViewModel : ViewModel() {
    private val _from : MutableLiveData<Boolean> = MutableLiveData()
    val etMassage : LiveData<Boolean> = _from

    fun fromMassageChange(message : String?) {
        message?.let {
            _from.value = isEmpty(it)
        }
    }


    private fun isEmpty(string: String): Boolean {
        return string.isNotEmpty()
    }
}
