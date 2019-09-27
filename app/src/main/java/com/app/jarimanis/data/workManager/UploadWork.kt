package com.app.jarimanis.data.workManager

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.app.jarimanis.R
import com.app.jarimanis.utils.NotificationID.CHANNEL_IDUPLOAD
import com.app.jarimanis.utils.NotificationID.N_IDUPLOAD

class UploadWork(appContext : Context, workerParameters: WorkerParameters) : CoroutineWorker(appContext,workerParameters) {
    override suspend fun doWork(): Result {
        return  try {
            val uploadData = inputData.keyValueMap
            displayNotification("My Worker", "Hey I finished my work");
        Result.success()
        }catch (throwable: Throwable){
            Result.failure()
        }
    }

    private fun  displayNotification(task:String , desc : String){
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val buildNotif = NotificationCompat.Builder(applicationContext,CHANNEL_IDUPLOAD)
            .setContentTitle(task)
            .setContentText(desc)
            .setSmallIcon(R.mipmap.ic_launcher)
        manager.notify(N_IDUPLOAD,buildNotif.build())
    }
}