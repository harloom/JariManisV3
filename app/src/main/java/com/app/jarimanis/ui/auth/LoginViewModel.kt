package com.app.jarimanis.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.jarimanis.data.datasource.models.token.get_token
import com.app.jarimanis.data.datasource.models.token.sent_token
import com.app.jarimanis.data.repository.firebase.TokenRepository
import retrofit2.Response

class LoginViewModel(private  val repository: TokenRepository) : ViewModel() {
    fun token(uid : String)  : LiveData<Response<get_token>> {
        val sent_ : sent_token = sent_token(uid)
        return  repository.getToken(sent_)
    }

}
