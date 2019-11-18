package com.app.jarimanis.ui.thread


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.jarimanis.data.datasource.models.SentEditThreads
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.data.repository.thread.ThreadsDataSource
import com.app.jarimanis.utils.NetworkState
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class ThreadListViewModel(
    val  categori: String,
    val  repo: ThreadRepository
) : ViewModel() {
    private val  _page: MutableLiveData<String> = MutableLiveData()
    private val _category : MutableLiveData<String>  = MutableLiveData()
    private val _onDelete = MutableLiveData<Boolean>()
    private val _onLike = MutableLiveData<OnLike>()
    private val reloadTrigger = MutableLiveData<Boolean>()
    val initialLoad: MutableLiveData<NetworkState> = MutableLiveData()
    data class OnLike (
         val item: Doc? =null,
         val position: Int? =null
    )

    init {
        initialLoad.postValue(NetworkState.LOADING)
        refress()
    }

    val onLike : LiveData<OnLike> = _onLike
    val onDelete : LiveData<Boolean> =  _onDelete
    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message
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
        super.onCleared()
        viewModelJob.cancel()

    }

    fun onRefress(){
        records.value!!.dataSource.invalidate()
    }

    fun likeThread(item: Doc, position : Int){
        CoroutineScope(IO).launch {
            val respon = repo.likeThread(item.id)
            withContext(Main){
                if(respon.isSuccessful){
                    val mLike = records.value?.snapshot()?.get(position)?.isLikes
                    if(mLike !=null ){
                        records.value?.snapshot()?.get(position)?.isLikes = !mLike
                        _onLike.value = OnLike(item,position)
                    }

                }
            }
        }
    }

    fun deleteThread(item: Doc) {
        CoroutineScope(Dispatchers.IO).launch {
            val respon = repo.deleteThread(item.id)
            withContext(Dispatchers.Main){
                _onDelete.value = respon.isSuccessful
                onRefress()
            }
        }

    }

    fun editThreads( editThreads: SentEditThreads){
        CoroutineScope(Dispatchers.IO).launch {
            val respon = repo.updateThread(editThreads)
            withContext(Dispatchers.Main){
                if(respon.isSuccessful){
                    println("respon edut : $respon")
                    onRefress()
                }else{
                    //handle err
                }
            }
        }
    }

    fun refress() {
        reloadTrigger.value = true
    }
}
