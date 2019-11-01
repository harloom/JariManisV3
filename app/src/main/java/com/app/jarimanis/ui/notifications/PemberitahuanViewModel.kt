package com.app.jarimanis.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.jarimanis.data.datasource.models.pemberitahuan.Doc
import com.app.jarimanis.data.repository.pemberitahuan.PemberitahuanDataSource
import com.app.jarimanis.data.repository.pemberitahuan.PemberitahuanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class PemberitahuanViewModel(private val repo: PemberitahuanRepository) : ViewModel() {

    fun cancelJobs(){
        repo.cancelJobs()
    }

    private val reloadTrigger = MutableLiveData<Boolean>()


    init {
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
        ).build()

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
