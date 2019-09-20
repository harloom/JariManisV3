package com.app.jarimanis.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.jarimanis.data.datasource.models.UserRegister
import com.app.jarimanis.data.repository.firebase.UserRepositoryImp
import com.app.jarimanis.ui.auth.utilRegister.RegisterResult
import com.google.firebase.auth.FirebaseAuth
import com.wajahatkarim3.easyvalidation.core.Validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val repo: UserRepositoryImp) : ViewModel() {
    private val TAG = "RegisterViewModel"

    private val _regResult = MutableLiveData<RegisterResult>()
    val regResult: LiveData<RegisterResult> = _regResult
    private val auth = FirebaseAuth.getInstance()

    fun registerProcess(email: String, pass: String, rePass: String) {
        val isEmailValid = Validator(email).nonEmpty().validEmail().addErrorCallback {
            _regResult.value = RegisterResult(onSuccess = false, onEmailEror = true, onError = it)
            return@addErrorCallback
        }.check()
        val isPasswordValid = Validator(pass).minLength(6).maxLength(20).addErrorCallback {
            _regResult.value = RegisterResult(onSuccess = false, onPasswordErr = true, onError = it)
        }.check()

        val isSamePassword = Validator(pass).textEqualTo(rePass).addErrorCallback {
            _regResult.value =
                RegisterResult(onSuccess = false, onRePasswordErr = true, onError = it)
        }.check()

        if (isEmailValid and isPasswordValid and isSamePassword) {
            auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener {

                val user = UserRegister(
                    _uid = it.user?.uid, nameUser = "user-" + System.currentTimeMillis(),
                    emailUser = it.user?.email
                )
                CoroutineScope(IO).launch {
                    try {
                        val respon = repo.saveUser(user)
                        if (respon.isSuccessful) {
                            withContext(Main) {
                                _regResult.value = RegisterResult(onSuccess = true)
                            }
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, e.toString())
                        withContext(Main) {
                            _regResult.value =
                                RegisterResult(onSuccess = false, onError = e.toString())
                        }

                    }

                }

            }.addOnFailureListener {
                _regResult.value = RegisterResult(onSuccess = false, onError = it.toString())
            }
        }


    }


}