package com.app.jarimanis.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.app.jarimanis.MainActivity

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.token.get_token
import com.github.loadingview.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.wajahatkarim3.easyvalidation.core.Validator
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jcodec.common.dct.SparseIDCT.finish
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response


class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var loadingDialog :   LoadingDialog
    private var loginJob  : Job? = null
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.goToRegister ->{
                navigation.navigate(R.id.action_loginFragment_to_registerFragment)
            }
            R.id.goToForgot->{
                navigation.navigate(R.id.action_loginFragment_to_forgotFragment)
            }
            R.id.login->{
                loginJob?.cancel()
                loginJob = CoroutineScope(Main).launch {
                    loadingDialog.show()
                    delay(500)
                    actionLogin(etEmail.text.toString(),etPassword.text.toString())
                }
            }
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    private  val vm : LoginViewModel by viewModel()
    private lateinit var navigation : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog.get(activity!!)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation = findNavController()
        goToRegister.setOnClickListener(this@LoginFragment)
        goToForgot.setOnClickListener(this@LoginFragment)
        login.setOnClickListener(this@LoginFragment)


    }
    private val subcribeToken = Observer<Response<get_token>>{
      if(it.isSuccessful){
          try {
              val getToken : get_token? = it.body()
              loadingDialog.hide()
              TokenUser.jwt =getToken!!.results
              if(!TokenUser.jwt.isNullOrBlank()){
                  goToMain()
              }

          }catch (e : Exception){
              loadingDialog.hide()
              Snackbar.make(viewId,"Terjadi Kesalahan silahkan login kembali",Snackbar.LENGTH_LONG).show()
          }


      }
        if(it.code() == 400){
            loadingDialog.hide()
            Snackbar.make(viewId,"Terjadi Kesalahan silahkan login kembali",Snackbar.LENGTH_LONG).show()
        }
    }


    private fun actionLogin(email : String , password : String){
        val isEmailValid = Validator(email).nonEmpty().validEmail().addErrorCallback {
            etEmail.error = it
        }.check()
        val isPasswordValid = Validator(password).minLength(6).maxLength(20).addErrorCallback {
            etPassword.error = it
        }.check()

        if (isEmailValid and isPasswordValid) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener {
                activity?.hideKeyboard()
                Snackbar.make(viewId,"Login Success..",Snackbar.LENGTH_LONG).show()
                vm.token(it.user!!.uid).observe(this@LoginFragment,subcribeToken)

            }.addOnFailureListener {
                loadingDialog.hide()
                activity?.hideKeyboard()
                Snackbar.make(viewId,"${it.message} , Please Try Again..",Snackbar.LENGTH_LONG).show()
            }
        }


    }


    private fun goToMain(){
        startActivity(Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
        activity?.finish()
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
    }

    private fun Context.hideKeyboard(view: View?) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}
