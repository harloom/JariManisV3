package com.app.jarimanis.data.service

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.utils.NotificationID




class PostIntentService() : JobIntentService() {
    private val JOBID: Int = 123

    override fun onCreate() {
        super.onCreate()
    }

    fun enqueueWork(context: Context, work: Intent) {
        enqueueWork(context, PostIntentService::class.java, JOBID, work)
    }
    override fun onHandleWork(intent: Intent) {
        val data = intent.getParcelableExtra<UploadThread>("upload")
        println(data)
        data?.let {

            displayNotification(it.title.toString(),it.content.toString())


        }

    }

    private fun  displayNotification(task:String , desc : String){
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val buildNotif = NotificationCompat.Builder(applicationContext,
            NotificationID.CHANNEL_IDUPLOAD
        )
            .setContentTitle(task)
            .setContentText(desc)
            .setSmallIcon(R.mipmap.ic_launcher)
        manager.notify(NotificationID.N_IDUPLOAD,buildNotif.build())
    }
}