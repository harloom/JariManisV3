package com.app.jarimanis

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.jarimanis.data.datasource.api.UserAPI
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.token.FirebaseToken
import com.app.jarimanis.data.repository.profile.ProfileRepository
import com.app.jarimanis.utils.DebugKey
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import java.lang.Exception

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if(destination.id == R.id.threadDetailFragment || destination.id ==R.id.threadListFragment ||
                destination.id == R.id.createThreadFragment || destination.id == R.id.roomFragment){
            navView.visibility = View.GONE

        }else{
            navView.visibility = View.VISIBLE
        }

    }
    @SuppressLint("ServiceCast")
    fun isOnline(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    private lateinit var navView : BottomNavigationView
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView= findViewById(R.id.nav_view)
        setSupportActionBar(toolbar)
        initIdProfile()
        initNotificationService()

        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener(this@MainActivity)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_chat,R.id.navigation_notifications,R.id.navigation_more))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            invalidateOptionsMenu()
            menuInflater.inflate(R.menu.top_item_menu, menu)
            return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
          val curentID =  navController.currentDestination!!.id
            menu?.findItem(R.id.action_search)?.isVisible = (curentID == R.id.navigation_home ||curentID == R.id.navigation_dashboard
                || curentID == R.id.navigation_chat ||   curentID == R.id.navigation_notifications
                || curentID == R.id.threadListFragment)


        return super.onPrepareOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_search -> {
            Toast.makeText(this@MainActivity,"SearchAction" , Toast.LENGTH_LONG).show()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        findNavController(R.id.nav_host_fragment).navigateUp()
        return super.onSupportNavigateUp()
    }


    private fun initNotificationService(){
        val api : UserAPI   = get()
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(DebugKey.key, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                try {
                    CoroutineScope(IO).launch {
                      val result =   api.putTokenNotification(FirebaseToken(token))
                        if(result.isSuccessful){
                            println("OK Token Sent")
                        }
                    }
                }catch (e : Exception){
                    println("Debug : ${e.toString()}")
                    initNotificationService()
                }


                // Log and toast

            })
    }

    private fun initIdProfile(){
        val repoProfile : ProfileRepository = get()
        val currentUid: String = FirebaseAuth.getInstance().currentUser?.uid !!
        val res = repoProfile.getProfile(currentUid)
        res.observe(this@MainActivity, Observer {
            TokenUser.idUser = it.id
        })
    }
}
