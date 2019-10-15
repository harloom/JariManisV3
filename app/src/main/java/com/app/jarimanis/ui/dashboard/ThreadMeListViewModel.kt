package com.app.jarimanis.ui.dashboard


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.data.repository.thread.ThreadsDataSource
import com.app.jarimanis.data.repository.thread.users.ThreadsUserDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class ThreadMeListViewModel(
    val uid : String,
    val  repo: ThreadRepository
) : ViewModel() {
    private val  _page: MutableLiveData<String> = MutableLiveData()




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
            ThreadsUserDataSource.Factory (uid,repo, uiScope),
            config
        ).build()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }


    fun deleteThreadS(item: Doc){

    }

    fun onRefress(){
        records.value!!.dataSource.invalidate()
    }



}
