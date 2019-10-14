package com.app.jarimanis.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.jarimanis.data.datasource.models.profile.Profile
import com.app.jarimanis.data.datasource.models.profile.Result
import com.app.jarimanis.data.repository.profile.ProfileRepository
import com.google.firebase.auth.FirebaseAuth

class DashboardViewModel (val repository: ProfileRepository) : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
     val myProfile : LiveData<Result> = repository.getProfile(mAuth.currentUser!!.uid)

    override fun onCleared() {
        super.onCleared()
    }

}