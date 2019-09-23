package com.app.jarimanis.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.kategori.Category
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori
import com.app.jarimanis.data.repository.webservice.JariManisAPI
import com.app.jarimanis.utils.DebugKey
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.koin.core.context.GlobalContext.get

class KategoriRepository (private val api: JariManisAPI) {
    var job : CompletableJob? = null

    fun getKategory() : LiveData<List<ResultKategori?>>{
        job = Job()
        return  object : LiveData<List<ResultKategori?>>(){
            override fun onActive() {
                super.onActive()
                job?.let { jb->
                    CoroutineScope(IO+ jb).launch {
                        try {
                            val respon = api.listCategory()
                            withContext(Main){
                                if(respon.isSuccessful){
                                    val result : Category? = respon.body()
                                    value = result?.resultKategori
                                }
                            }
                        }catch (e : Exception){
                            Log.d(DebugKey.key, e.toString());
                        }
                    }

                }
            }
        }
    }

    fun cancel(){
        job?.cancel()
    }
}