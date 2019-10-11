package com.app.jarimanis.ui.dashboard

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.profile.Result
import com.app.jarimanis.utils.NotificationID
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {


    private val  vm: DashboardViewModel by viewModel()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title ="Akun Saya"
        vm.myProfile.observe(this@DashboardFragment,subcribeProfile)
        buttonOnclick()
    }

    private fun buttonOnclick() {
        btnPengaturanUser.setOnClickListener {
            val manager =
                context!!.getSystemService(    Context.NOTIFICATION_SERVICE) as NotificationManager
            val buildNotif = NotificationCompat.Builder(
                context!!, NotificationID.CHANNEL_ID2

            )
                .setContentTitle("1")
                .setContentText("Message")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroup(NotificationID.GROUP_MESSAGE).build()


            manager.notify(200, buildNotif)
        }
    }

    private val subcribeProfile = Observer<Result> {
        initProfile(it)
    }

    private  fun initProfile(items : Result){
        try {
            tv_user.text = items.nameUser
            Glide.with(this@DashboardFragment).load(items.thumbail).into(iv_thumbail)
        }catch (e: Exception){
            Toast.makeText(context!!,"Something Erorr, please refress!" , Toast.LENGTH_LONG).show()
        }
    }
}