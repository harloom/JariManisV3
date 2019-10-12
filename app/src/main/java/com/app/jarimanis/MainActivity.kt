package com.app.jarimanis

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import androidx.preference.PreferenceManager
import com.app.jarimanis.data.datasource.api.UserAPI
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.token.FirebaseToken
import com.app.jarimanis.data.repository.profile.ProfileRepository
import com.app.jarimanis.utils.DebugKey
import com.app.jarimanis.utils.Key.CHATEXITS
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.badge.BadgeDrawable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import me.ibrahimsn.library.LiveSharedPreferences
import org.koin.android.ext.android.get
import java.lang.Exception


class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if(destination.id == R.id.navigation_home || destination.id ==R.id.navigation_chat || destination.id == R.id.navigation_dashboard ||
                destination.id == R.id.navigation_more || destination.id == R.id.navigation_notifications){
            navView.visibility = View.VISIBLE

        }else{
            navView.visibility = View.GONE
        }

    }





    @Suppress("DEPRECATION")
    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
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



        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_chat,R.id.navigation_notifications,R.id.navigation_more))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        subcribeLivePrefrences()
    }

    private fun subcribeLivePrefrences() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        val liveSharedPreferences = LiveSharedPreferences(preferences)
        val badgeDrawable  = navView.getOrCreateBadge(R.id.navigation_chat)
        liveSharedPreferences.getBoolean(CHATEXITS,false).observe(this@MainActivity, Observer { value->
                badgeDrawable.isVisible = value

        })
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
