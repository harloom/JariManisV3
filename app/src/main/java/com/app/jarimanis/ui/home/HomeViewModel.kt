package com.app.jarimanis.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.app.jarimanis.data.datasource.models.kategori.Category
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori

class HomeViewModel(val repository: KategoriRepository) : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text
    private val reloadTrigger = MutableLiveData<Boolean>()
    val _kategori  : LiveData<List<ResultKategori?>> = Transformations.switchMap(reloadTrigger){
        repository.getKategory()
    }


    init {
        refress()
    }

    val kategori  : LiveData<List<ResultKategori?>> = _kategori


    fun refress() {
        reloadTrigger.value = true
    }
    fun  jobCancel () {
        repository.cancel()
    }
}