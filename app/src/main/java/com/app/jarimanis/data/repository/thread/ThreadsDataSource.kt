package com.app.jarimanis.data.repository.thread

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.utils.DebugKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ThreadsDataSource ( private val category :String,
                          private val repo : ThreadRepository ,
                          private val uiScope: CoroutineScope
): PageKeyedDataSource<String,Doc>(){
    class Factory(
        private val category: String,
        private val repo :   ThreadRepository,
        private val uiScope: CoroutineScope
    ): DataSource.Factory<String, Doc>() {
        override fun create(): DataSource<String, Doc> {
            return  ThreadsDataSource(category =category, repo = repo,uiScope = uiScope)
        }

    }



    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Doc>
    ) {
        uiScope.launch {
            val respon = repo.getPaging( category,"1")
            if(respon.isSuccessful){
                val items = respon.body()
                val result = items?.result !!
                callback.onResult(result.docs!!,0,result.totalDocs!!
                    , result.prevPage, result.nextPage
                )
            }

        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Doc>) {

        uiScope.launch {
            val page = params.key
            Log.d(DebugKey.key,"Page : ${page}");
            if(page != "null"){
                val respon = repo.getPaging(category,page)
                if(respon.isSuccessful){
                    val items = respon.body()
                    val result = items?.result !!
                    callback.onResult(result.docs!!,result.nextPage)
                }

//                callback.onResult(items.results.docs,items.results.prevPage.toString())
            }else{
                //handle error
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Doc>) {
        uiScope.launch {
            val page = params.key
            Log.d(DebugKey.key,"Page : ${page}");
            if(page != "null"){
                val respon = repo.getPaging(category,page)
                if(respon.isSuccessful){
                    val items = respon.body()
                    val result = items?.result !!
                    callback.onResult(result.docs!!,result.prevPage)
                }

//                callback.onResult(items.results.docs,items.results.prevPage.toString())
            }else{
                //handle error
            }
        }
    }


}