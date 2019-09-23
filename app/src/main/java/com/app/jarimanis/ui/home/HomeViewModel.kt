package com.app.jarimanis.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.jarimanis.data.datasource.models.kategori.Category
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori

class HomeViewModel(val repository: KategoriRepository) : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text


    val kategori  : LiveData<List<ResultKategori?>> = repository.getKategory()

    fun  jobCancel () {
        repository.cancel()
    }
}