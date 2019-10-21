package com.app.jarimanis.ui.thread.comentar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.jarimanis.data.datasource.models.diskusi.paging.Doc
import com.app.jarimanis.data.repository.commentar.DiskusiDataSource
import com.app.jarimanis.data.repository.commentar.DiskusiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class KomentarViewModel(
    private val categori: String,
    private val repo: DiskusiRepository
) : ViewModel(){
    private val  _page: MutableLiveData<String> = MutableLiveData()
    private val idDoc : MutableLiveData<String>  = MutableLiveData()
    private val reloadTrigger = MutableLiveData<Boolean>()


//    init {
//        refress()
//    }
//


//    fun init(subId :String ,page: String){
//        val pageUpdate = page
//        val subIdUpdate = subId
//        if (_page.value == pageUpdate) {
//            return
//        }
//
//        if(idDoc.value == subIdUpdate){
//            return
//        }
//        idDoc.value = subIdUpdate
//        _page.value = pageUpdate
//    }

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
            DiskusiDataSource.Factory (categori,repo, uiScope),
            config
        ).build()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }


//    fun refress() {
//        reloadTrigger.value = true
//    }

     fun onRefress(){
        records.value!!.dataSource.invalidate()
    }
}