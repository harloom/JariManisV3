package com.app.jarimanis.data.repository.thread

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.app.jarimanis.data.datasource.models.thread.Doc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ThreadsDataSource ( private val category :String,
                          private val repo : ThreadRepository ,
                          private val uiScope: CoroutineScope
): PageKeyedDataSource<String,Doc>(){
    class Factory(
        private val subId: String,
        private val repo :   ThreadRepository,
        private val uiScope: CoroutineScope
    ): DataSource.Factory<String, Doc>() {
        override fun create(): DataSource<String, Docs> {
            return  ThreadsDataSource(category =subId, repo = repo,uiScope = uiScope)
        }

    }



    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Doc>
    ) {
        uiScope.launch {
//            val items = repo.getPaging( subId,"1")
            Log.d("MenuDataSource" , "list size  : ${items.results.docs.size}")
            val pagging = items.results
            callback.onResult(items.results.docs,0,pagging.totalDocs
                , pagging.prevPage, pagging.nextPage
            )
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Doc>) {

    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Doc>) {

    }


}