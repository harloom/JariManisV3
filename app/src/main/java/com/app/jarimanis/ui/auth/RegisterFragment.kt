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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.app.jarimanis.MainActivity

import com.app.jarimanis.R
import com.github.loadingview.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back_arrow->{
                findNavController().navigateUp()
            }
            R.id.register->{
                jobRegister?.cancel()
                jobRegister = CoroutineScope(Main).launch {
                    loadingDialog.show()
                    delay(300)
                    vm.registerProcess(email = etEmail.text.toString(),
                        pass = etpassword.text.toString() ,
                        rePass =  etRepassword.text.toString())
                }

            }
        }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var loadingDialog: LoadingDialog
    private val  vm: RegisterViewModel by viewModel()
    private var jobRegister : Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog.get(activity!!)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        iv_back_arrow.setOnClickListener(this@RegisterFragment)
        register.setOnClickListener(this@RegisterFragment)
    }

    private fun initViewModel(){
        vm.regResult.observe(this@RegisterFragment, Observer {
            if (it.onSuccess == false and !it.onError.isNullOrBlank()){
                loadingDialog.hide()
                if(it.onEmailEror){
                    etEmail.error = it.onError.toString()
                }else if(it.onPasswordErr){
                    etpassword.error = it.onError.toString()
                }else if(it.onRePasswordErr){
                    etRepassword.error = it.onError.toString()
                }
            }else{
                loadingDialog.hide()
                MaterialDialog(context!!).show {
                    title(R.string.pemberitahuan)
                    message(R.string.pendaftaranBerhasil)
                    icon(drawable = ContextCompat.getDrawable(context,R.mipmap.ic_launcher))
                    cornerRadius(16f)
                    positiveButton(text = "Ok"){
                        goToMain()
                    }

                }

            }
        })
    }

    private fun goToMain(){
        vm.logOut()
        findNavController().navigateUp()

    }

    private fun snackbar(s : String){
        Snackbar.make(view!!,s , Snackbar.LENGTH_LONG)
            .show()
        activity?.hideKeyboard()
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
    }

    fun Context.hideKeyboard(view: View?) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }



    fun onRegister(view: View) {
        vm.registerProcess(email = etEmail.text.toString(),pass = etpassword.text.toString() ,rePass =  etRepassword.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }



}
