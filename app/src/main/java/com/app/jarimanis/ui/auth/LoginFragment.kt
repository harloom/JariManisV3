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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.app.jarimanis.MainActivity

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.UserRegister
import com.app.jarimanis.data.datasource.models.token.get_token
import com.app.jarimanis.data.repository.firebase.UserRepository
import com.app.jarimanis.data.repository.firebase.UserRepositoryImp
import com.app.jarimanis.utils.debounce.onClickDebounced
import com.github.florent37.inlineactivityresult.kotlin.coroutines.startForResult
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import com.github.loadingview.LoadingDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.wajahatkarim3.easyvalidation.core.Validator
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.jcodec.common.dct.SparseIDCT.finish
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response


class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var loadingDialog :   LoadingDialog
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private  val firebaseAuth = FirebaseAuth.getInstance()
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
        initConfigure()


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sign_in_with_google.onClickDebounced {
            signWithGoogle()
        }
    }

    private fun signWithGoogle() {
        loadingDialog.show()
        val signInIntent = mGoogleSignInClient.signInIntent
        startForResult(signInIntent){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleResult(task)
        }.onFailed {

        }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        task.addOnSuccessListener {
            firebaseAuthWithGoogle(it)
        }.addOnFailureListener {
            Toast.makeText(context, "Google sign in failed:(", Toast.LENGTH_LONG).show()
        }

    }

    private fun firebaseAuthWithGoogle(
        acct: GoogleSignInAccount
    ) {
        val repo : UserRepositoryImp by inject()
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                    val user = UserRegister(
                        _uid = it.result!!.user!!.uid, nameUser = acct.displayName,
                        emailUser = acct.email,thumbail = acct.photoUrl.toString()
                    )
                    CoroutineScope(IO).launch {
                        try {
                            val respon = repo.saveUser(user)
                            if (respon.isSuccessful) {
                                withContext(Main) {
                                    vm.token(it.result!!.user!!.uid).observe(this@LoginFragment,subcribeToken)

                                }
                            }else{
                               if(respon.code() == 409){
                                   withContext(Main){
                                       vm.token(it.result!!.user!!.uid).observe(this@LoginFragment,subcribeToken)
                                   }
                               }
                            }
                        } catch (e: Exception) {
                            println("${e.message}")

                        }
                    }

            } else {
                Toast.makeText(context, "Google sign in failed:(", Toast.LENGTH_LONG).show()

            }

        }

    }


    private fun initConfigure() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)

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
