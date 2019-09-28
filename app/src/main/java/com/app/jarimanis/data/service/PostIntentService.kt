package com.app.jarimanis.data.service

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.ImageX
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import com.app.jarimanis.data.datasource.models.thread.VideoX
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.utils.NotificationID
import com.app.jarimanis.utils.NotificationID.GROUP_UPLOAD
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File
import java.util.*
import kotlin.properties.Delegates


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PostIntentService() : JobIntentService() {
    private val JOBID: Int = 123
    val metadata = StorageMetadata.Builder()
        .setContentType("image/jpg")
        .build()

    override fun onCreate() {
        super.onCreate()
    }

    fun enqueueWork(context: Context, work: Intent) {
        enqueueWork(context, PostIntentService::class.java, JOBID, work)
    }

    val imageUrl = mutableListOf<ImageX>()
    val videoUrl = mutableListOf<VideoX>()
    private var data: UploadThread? = null


    override fun onHandleWork(intent: Intent) {
        data = intent.getParcelableExtra<UploadThread>("upload")
        data?.let {
            try {
                if(it.images?.isNotEmpty()!!){
                    uploadImage()
                }else if(it.videos?.isNotEmpty()!!){
                    uploadVideo()
                }else{
                    uploadToNodeServer()
                }

            } catch (e: Exception) {

            }


        }

    }

    private fun uploadVideo(){
    val storage = FirebaseStorage.getInstance()
        data?.let { data_ ->
            data_.videos?.forEachIndexed { index, iv ->
                val file = Uri.fromFile(File(iv?._url))
                val riversRef = storage.reference.child("videos/${UUID.randomUUID()}")
                videoUrl.add(VideoX(riversRef.toString()))
                val uploadTask = riversRef.putFile(file)
                uploadTask.addOnProgressListener {
                    val progress =
                        (100.0 * it.bytesTransferred) / it.totalByteCount
                    displayNotificationProgress(index + 1, progression = progress.toInt())
                }.addOnSuccessListener {
                    cancelNotif(NotificationID.N_IDPROGRESSUPLOAD + index + 1)
                    if (index == data_.videos.size - 1) {
                        uploadToNodeServer()
                    }
                }.addOnFailureListener {
                    //
                }

            }
        }
    }


    private fun uploadImage() {
        val storage = FirebaseStorage.getInstance()
        data?.let { data_ ->
            data_.images?.forEachIndexed { index, im ->
                    val file = Uri.fromFile(File(im?._url))
                    val riversRef = storage.reference.child("post/${UUID.randomUUID()}")
                    imageUrl.add(ImageX(riversRef.toString()))
                    val uploadTask = riversRef.putFile(file, metadata)
                    uploadTask.addOnProgressListener {
                    val progress =
                        (100.0 * it.bytesTransferred) / it.totalByteCount
                    displayNotificationProgress(index + 1, progression = progress.toInt())
                    }.addOnSuccessListener {
                    cancelNotif(NotificationID.N_IDPROGRESSUPLOAD + index + 1)
                    if (index == data_.images.size - 1) {
                        uploadToNodeServer()
                    }
                    }.addOnFailureListener {
                    //
                    }

                }
            }

        }






private fun uploadToNodeServer() {
    data?.let {
        val repo: ThreadRepository by inject()
        val upload = UploadThread(it.category,it.content,imageUrl,it.title,videoUrl)
        CoroutineScope(IO).launch {
            val respon = repo.postThread(upload)
            if (respon.isSuccessful) {
                displayNotification("Upload Berhasil", it.content.toString())
            } else {
                displayNotification(it.title.toString(), "Gagal")
            }
        }
    }
}

private fun displayNotification(task: String, desc: String) {
    val manager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val buildNotif = NotificationCompat.Builder(
        applicationContext,
        NotificationID.CHANNEL_IDUPLOAD
    )
        .setContentTitle(task)
        .setContentText(desc)
        .setSmallIcon(R.mipmap.ic_launcher)
    manager.notify(NotificationID.N_IDUPLOAD, buildNotif.build())
}

private fun displayNotificationProgress(index: Int, progression: Int) {
    val manager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val buildNotif = NotificationCompat.Builder(
        applicationContext,
        NotificationID.CHANNEL_IDUPLOAD
    )
        .setContentTitle("Uploading $index")
        .setContentText("File to Server..")
        .setProgress(100, progression, false)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setGroup(GROUP_UPLOAD)
    manager.notify(NotificationID.N_IDPROGRESSUPLOAD + index, buildNotif.build())
}

private fun cancelNotif(id: Int) {
    val manager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.cancel(id)
}

}