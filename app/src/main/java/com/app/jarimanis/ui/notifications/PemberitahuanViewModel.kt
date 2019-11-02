package com.app.jarimanis.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.jarimanis.data.datasource.models.pemberitahuan.Doc
import com.app.jarimanis.data.repository.pemberitahuan.PemberitahuanDataSource
import com.app.jarimanis.data.repository.pemberitahuan.PemberitahuanRepository
import com.app.jarimanis.utils.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class PemberitahuanViewModel(private val repo: PemberitahuanRepository) : ViewModel() {

    fun cancelJobs(){
        repo.cancelJobs()
    }

    private val reloadTrigger = MutableLiveData<Boolean>()
    val initialLoad: MutableLiveData<NetworkState> = MutableLiveData()

    init {
        initialLoad.postValue(NetworkState.LOADING)
        refress()
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
            PemberitahuanDataSource.Factory(
                repo,
                uiScope
            ),
            config
        ).setBoundaryCallback(object : PagedList.BoundaryCallback<Doc>(){
            override fun onZeroItemsLoaded() {
                super.onZeroItemsLoaded()
                initialLoad.postValue(NetworkState.EMPTY)
            }

            override fun onItemAtEndLoaded(itemAtEnd: Doc) {
                super.onItemAtEndLoaded(itemAtEnd)
            }

            override fun onItemAtFrontLoaded(itemAtFront: Doc) {
                super.onItemAtFrontLoaded(itemAtFront)
                initialLoad.postValue(NetworkState.LOADED)
            }
        }).build()

    override fun onCleared() {
        viewModelJob.cancel()

    }


    fun refress() {
        reloadTrigger.value = true
    }

    fun onRefress(){
        records.value!!.dataSource.invalidate()
    }

}
