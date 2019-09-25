package com.app.jarimanis.ui.thread


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.data.repository.thread.ThreadsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

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


    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPrefetchDistance(8)
        .setPageSize(8)
        .build()

    val records  : LiveData<PagedList<Doc>> =
        LivePagedListBuilder<String, Doc>(
            ThreadsDataSource.Factory (categori,repo, uiScope),
            config
        ).build()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }

    fun onRefress(){
        records.value!!.dataSource.invalidate()
    }



}
