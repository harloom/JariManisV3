package com.app.jarimanis.data.service

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.NewFile
import com.app.jarimanis.data.datasource.models.thread.Status.ErrorUpload
import com.app.jarimanis.data.datasource.models.thread.Status.InProgress
import com.app.jarimanis.data.datasource.models.thread.Status.Success
import com.app.jarimanis.data.datasource.models.thread.UploadStatus
import com.app.jarimanis.data.datasource.models.thread.UrlUpload
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.data.service.result_reciver.UploadReciver
import com.app.jarimanis.utils.NotificationID
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import retrofit2.Response
import java.io.File
import java.util.*

class NewFileService : JobIntentService() {

    companion object{
        val UPLOAD_IMAGE = 1
        val UPLOAD_VIDEO = 2
        val KEY_NEWFILE = "upload_new_item"
        const val RECEIVER_NEWFILE = "$PACKAGE_NAME.RECEIVER_NEWFILE"
        const val SUCCESS_RESULT = 100
        const val RESULT_DATA_KEY = "${PACKAGE_NAME}.RESULT_DATA_KEY"
//        const val STATUS_UPLOAD_DATA = "${PACKAGE_NAME}.RESULT_STATUS_UPLOAD"
    }

    override fun onCreate() {
        super.onCreate()
    }

    private var receiver: UploadReciver? = null
    private val JOBID = 124
    fun enqueueWork(context: Context, work: Intent) {
        println("start servoce")
        enqueueWork(context, PostIntentService::class.java, JOBID, work)
    }


    private var data: NewFile? = null

    override fun onHandleWork(intent: Intent) {
        println("uploadImage")
        receiver = intent.getParcelableExtra(RECEIVER_NEWFILE)
        data = intent.getParcelableExtra<NewFile>(KEY_NEWFILE)
        data?.let {
            if(it.jenis == UPLOAD_IMAGE && it.path.isNotEmpty()){
                uploadImage()
            }else if(it.jenis == UPLOAD_VIDEO && it.path.isNotEmpty()){
                uploadVideo()
            }
        }

    }
    private fun deliverResultToReceiver(resultCode: Int, mUpload: UploadStatus) {
            val bundle = Bundle().apply { putParcelable(RESULT_DATA_KEY, mUpload) }
            receiver?.send(resultCode, bundle)
    }


    private fun uploadVideo() {
        val storage = FirebaseStorage.getInstance()
        data?.let { data_ ->
                val file = Uri.fromFile(File(data_.path))
                val riversRef = storage.reference.child("videos/${UUID.randomUUID()}")
                val uploadTask = riversRef.putFile(file)
                uploadTask.addOnProgressListener {
                    val progress =
                        (100.0 * it.bytesTransferred) / it.totalByteCount
                    displayNotificationProgress(progression = progress.toInt())
                    deliverResultToReceiver(SUCCESS_RESULT, UploadStatus(InProgress, progress.toInt(),"Sedang Upload"))
                }.addOnSuccessListener {
                    cancelNotif(NotificationID.N_IDPROGRESSUPLOAD)
                    uploadToNodeServer(riversRef)

                }.addOnFailureListener {

            }
        }
    }





    private fun uploadToNodeServer(riversRef: StorageReference) {
        data?.let {
            val repo: ThreadRepository by inject()
            val upload = UrlUpload(riversRef.toString())
            CoroutineScope(IO).launch {
                if(it.jenis == UPLOAD_IMAGE){
                     val respon = repo.newImage(it.idThread,upload).await()
                    if (respon.isSuccessful) {
                        deliverResultToReceiver(SUCCESS_RESULT,UploadStatus(Success,null,"Upload",riversRef.toString()))
                        displayNotification("Pemberitahuan Upload"," Foto baru berhasil di tambahkan")
                    } else {
                        deliverResultToReceiver(SUCCESS_RESULT,UploadStatus(ErrorUpload,null,"Upload"))
                        displayNotification("Pemberitahuan Upload", "Gagal. Mohon upload kembali")
                    }
                }

                if(it.jenis == UPLOAD_VIDEO){
                    val respon = repo.newVideo(it.idThread,upload).await()
                    if (respon.isSuccessful) {
                        deliverResultToReceiver(SUCCESS_RESULT,UploadStatus(Success,null,"Upload",riversRef.toString()))
                        displayNotification("Pemberitahuan Upload"," Video baru berhasil di tambahkan")
                    } else {
                        deliverResultToReceiver(SUCCESS_RESULT,UploadStatus(ErrorUpload,null,"Upload"))
                        displayNotification("Pemberitahuan Upload", "Gagal. Mohon upload kembali")
                    }
                }


            }
        }
    }


    private fun uploadImage() {
        val storage = FirebaseStorage.getInstance()
        data?.let { data_ ->

                val file = Uri.fromFile(File(data_.path))
                val riversRef = storage.reference.child("post/${UUID.randomUUID()}")
                val uploadTask = riversRef.putFile(file)
                uploadTask.addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
                    displayNotificationProgress( progression = progress.toInt())
                    deliverResultToReceiver(SUCCESS_RESULT, UploadStatus(InProgress, progress.toInt(),"Sedang Upload"))
                }.addOnSuccessListener {
                    cancelNotif(NotificationID.N_IDPROGRESSUPLOAD)
                    uploadToNodeServer(riversRef)
                }.addOnFailureListener {
                    //
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

    private fun displayNotificationProgress(progression: Int) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val buildNotif = NotificationCompat.Builder(
            applicationContext,
            NotificationID.CHANNEL_IDUPLOAD
        )
            .setContentTitle("Uploading")
            .setContentText("File to Server..")
            .setProgress(100, progression, false)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setGroup(NotificationID.GROUP_UPLOAD)
        manager.notify(NotificationID.N_IDPROGRESSUPLOAD, buildNotif.build())
    }

    private fun cancelNotif(id: Int) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(id)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStopCurrentWork(): Boolean {
        return super.onStopCurrentWork()
    }

}