package com.app.jarimanis.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.jarimanis.LoginActivity
import com.app.jarimanis.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_mores.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MoreFragment : Fragment(), View.OnClickListener {
    private var jobClick: Job? = null
    override fun onClick(v: View?) {
        jobClick?.cancel()
        jobClick = CoroutineScope(Main).launch {
            when(v!!.id){
                R.id.action_logOut->{
                    logOut()
                }
            }
        }
    }

    private lateinit var notificationsViewModel: MoreViewModel
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_mores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title ="Lainnya"
        action_logOut.setOnClickListener(this@MoreFragment)
    }

    private fun logOut(){
        mAuth.signOut()
        startActivity(Intent(context,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
        activity?.finish()
    }
}