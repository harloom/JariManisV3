package com.app.jarimanis.ui.thread


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.jarimanis.data.repository.thread.ThreadRepository

class ThreadListViewModel(
    val  categori: String,
    val  repo: ThreadRepository
) : ViewModel() {
    private val  _page: MutableLiveData<String> = MutableLiveData()
    private val _category : MutableLiveData<String>  = MutableLiveData()

    fun init(subId :String ,page: String){
        val pageUpdate = page
        val subIdUpdate = subId
        if (_page.value == pageUpdate) {
            return
        }

        if(_category.value == subIdUpdate){
            return
        }
        _category.value = subIdUpdate
        _page.value = pageUpdate
    }

    fun cancelJobs(){
        repo.cancelJobs()
    }
}
