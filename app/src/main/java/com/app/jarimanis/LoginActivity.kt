package com.app.jarimanis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.app.jarimanis.data.datasource.local.TokenUser
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val navController = findNavController(R.id.nav_auth)

        if(FirebaseAuth.getInstance().currentUser != null && !TokenUser.jwt.isNullOrBlank()){
            goToMain()
        }else{

        }
    }


    private fun goToMain(){
        startActivity(Intent(this@LoginActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }
}
