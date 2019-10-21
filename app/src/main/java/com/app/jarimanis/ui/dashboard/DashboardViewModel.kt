package com.app.jarimanis.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.app.jarimanis.data.datasource.models.profile.Result
import com.app.jarimanis.data.repository.profile.ProfileRepositoryImp
import com.google.firebase.auth.FirebaseAuth

class DashboardViewModel (val repositoryImp: ProfileRepositoryImp) : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
     val text: LiveData<String> = _text
     private val reloadTrigger = MutableLiveData<Boolean>()


     val myProfile : LiveData<Result>  = Transformations.switchMap(reloadTrigger){
         repositoryImp.getProfile(mAuth.currentUser!!.uid)
     }
    init {
        refress()
    }


    override fun onCleared() {
        super.onCleared()
    }
    fun refress() {
        reloadTrigger.value = true
    }
}