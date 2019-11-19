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
import com.app.jarimanis.utils.NetworkState
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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
    val initialLoad: MutableLiveData<NetworkState> = MutableLiveData()

    init {
        initialLoad.postValue(NetworkState.LOADING)
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

    var records  : LiveData<PagedList<Doc>> =
        LivePagedListBuilder<String, Doc>(
            DiskusiDataSource.Factory (categori,repo, uiScope),
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


    fun likeKomentar(
        item: Doc,
        position: Int
    ) {
        CoroutineScope(IO).launch {
            val respon = repo.likeDiskusi(item.id)
            withContext(Main){
                if(respon.isSuccessful){
                    val mLike = records.value?.snapshot()?.get(position)?.isLikes
                    if(mLike !=null ){
//                        records.value?.snapshot()?.get(position)?.isLikes = !mLike
//                        _onLike.value = OnLike(item,position)
                    }

                }
            }
        }
    }


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