package com.app.jarimanis.ui.dashboard


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.jarimanis.data.datasource.models.SentEditThreads
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.data.repository.thread.users.ThreadsUserDataSource
import com.app.jarimanis.utils.NetworkState
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class ThreadMeListViewModel(
    val uid : String,
    val  repo: ThreadRepository
) : ViewModel() {
    private val  _page: MutableLiveData<String> = MutableLiveData()

    fun cancelJobs(){
        repo.cancelJobs()
    }
    private val _onDelete = MutableLiveData<Boolean>()
    val onDelete : LiveData<Boolean> =  _onDelete


    private val _message = MutableLiveData<String?>()
    val message : LiveData<String?> = _message
    val initialLoad: MutableLiveData<NetworkState> = MutableLiveData()
    init {
        _onDelete.value = false
        initialLoad.postValue(NetworkState.LOADING)
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


    fun deleteThread(item: Doc) {
        CoroutineScope(IO).launch {
            val respon = repo.deleteThread(item.id)
            withContext(Main){
                _onDelete.value = respon.isSuccessful
                onRefress()
            }
        }

    }

    fun editThreads( editThreads: SentEditThreads){
        CoroutineScope(IO).launch {
            val respon = repo.updateThread(editThreads)
            withContext(Main){
                if(respon.isSuccessful){
                    println("respon edut : $respon")
                    onRefress()
                }else{
                    //handle err
                }
            }
        }
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

                    }

                }
            }
        }
    }



}
