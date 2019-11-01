package com.app.jarimanis.data.repository.pemberitahuan

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.app.jarimanis.data.datasource.models.pemberitahuan.Doc


import com.app.jarimanis.utils.DebugKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PemberitahuanDataSource (
                               private val repo : PemberitahuanRepository,
                               private val uiScope: CoroutineScope
): PageKeyedDataSource<String, Doc>(){
    class Factory(
        private val repo :   PemberitahuanRepository,
        private val uiScope: CoroutineScope
    ): DataSource.Factory<String,Doc >() {
        override fun create(): DataSource<String, Doc> {
            return  PemberitahuanDataSource(repo = repo,uiScope = uiScope)
        }

    }



    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Doc>
    ) {
        uiScope.launch {
            val respon = repo.getPaging( "1")
            if(respon.isSuccessful){
                val items = respon.body()
                println(items)
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
            if(page != "null"){
                val respon = repo.getPaging(page)
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
                val respon = repo.getPaging(page)
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