package com.app.jarimanis.ui.thread.detail.comentar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.jarimanis.data.datasource.models.diskusi.ResponPostComentar
import com.app.jarimanis.data.datasource.models.diskusi.SaveCommentar
import com.app.jarimanis.data.datasource.models.diskusi.paging.Doc
import com.app.jarimanis.data.repository.commentar.DiskusiDataSource
import com.app.jarimanis.data.repository.commentar.DiskusiRepository
import kotlinx.coroutines.*
import retrofit2.Response

class KomentarViewModel(
    private val categori: String,
    private val repo: DiskusiRepository
) : ViewModel(){
    private val _from : MutableLiveData<Boolean> = MutableLiveData()
    val etKomentar : LiveData<Boolean> = _from
    private val  _respon: MutableLiveData<Response<ResponPostComentar>> = MutableLiveData()
     val  respon : LiveData<Response<ResponPostComentar>?> = _respon
    private val reloadTrigger = MutableLiveData<Boolean>()
    private val _onDelete = MutableLiveData<StatusChange>()
    val onDelete : LiveData<StatusChange> =  _onDelete

    init {
        refress()
    }
    fun fromMassageChange(message : String?) {
        message?.let {
            _from.value = it.isNotEmpty()
        }
    }

    fun deleteCommentar(
        item: Doc,
        position: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val respon = repo.deleteDiskusi(item.id!!)
            withContext(Dispatchers.Main){
                println("Respon Error " + respon.code())
                if(respon.isSuccessful){
                    _onDelete.value = StatusChange(respon.isSuccessful,position)
                }else{
                    _onDelete.value = StatusChange(false,position)
                }

            }
        }

    }

    suspend fun postCommentar(id : String , comentar : SaveCommentar){
        val res = repo.postDiskusi(id,comentar)
        if(res.isSuccessful){
            onRefress()
        }else{
            _respon.postValue(res)
        }
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
            DiskusiDataSource.Factory (categori,repo, uiScope),
            config
        ).build()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }


    fun refress() {
        reloadTrigger.value = true
    }

     fun onRefress(){
        records.value!!.dataSource.invalidate()
    }


}