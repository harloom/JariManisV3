package com.app.jarimanis.data.service

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.support.v4.os.ResultReceiver
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
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.io.File
import java.util.*

class NewFileService : JobIntentService() {

    companion object {
        val UPLOAD_IMAGE = 1
        val UPLOAD_VIDEO = 2
        val KEY_NEWFILE = "upload_new_item"
        const val KEY_THREADID = "thread_id"
        const val RECEIVER_NEWFILE = "$PACKAGE_NAME.RECEIVER_NEWFILE"
        const val SUCCESS_RESULT_IMAGE = 100
        const val SUCCESS_RESULT_VIDEO = 101
        const val RESULT_DATA_KEY = "${PACKAGE_NAME}.RESULT_DATA_KEY"
//        const val STATUS_UPLOAD_DATA = "${PACKAGE_NAME}.RESULT_STATUS_UPLOAD"
    }

    override fun onCreate() {
        super.onCreate()
    }

    private var receiver: ResultReceiver? = null
    private val JOBID = 124
    fun enqueueWork(context: Context, work: Intent) {
        println("start servoce")
        enqueueWork(context, NewFileService::class.java, JOBID, work)
    }


    override fun onHandleWork(intent: Intent) {
        println("uploadImage")
        val idThread = intent.extras?.getString(KEY_THREADID)
        receiver   = intent.getParcelableExtra(RECEIVER_NEWFILE)
        val data = intent.getParcelableExtra<NewFile>(KEY_NEWFILE)
        data?.let {
            if (it.jenis == UPLOAD_IMAGE && it.path.isNotEmpty()) {
                uploadImage(idThread, data)
            } else if (it.jenis == UPLOAD_VIDEO && it.path.isNotEmpty()) {
                uploadVideo(idThread, data)
            }
        }

    }

    private fun deliverResultToReceiver(requestCode: Int, mUpload: UploadStatus) {
        val bundle = Bundle().apply { putParcelable(RESULT_DATA_KEY, mUpload) }
        receiver?.send(requestCode, bundle)
    }


    private fun uploadVideo(
        idThread: String?,
        data: NewFile?
    ) {
        val storage = FirebaseStorage.getInstance()
        data?.let { data_ ->
            val file = Uri.fromFile(File(data_.path))
            val riversRef = storage.reference.child("videos/${UUID.randomUUID()}")
            val uploadTask = riversRef.putFile(file)
            uploadTask.addOnProgressListener {
                val progress =
                    (100.0 * it.bytesTransferred) / it.totalByteCount
                displayNotificationProgress(progression = progress.toInt())
                deliverResultToReceiver(
                    SUCCESS_RESULT_VIDEO,
                    UploadStatus(InProgress, progress.toInt(), "Sedang Upload")
                )
            }.addOnSuccessListener {
                cancelNotif(NotificationID.N_IDPROGRESSUPLOAD)
                uploadToNodeServer(idThread, data, riversRef)

            }.addOnFailureListener {

            }
        }
    }


    private fun uploadToNodeServer(idThread: String?, data: NewFile?, riversRef: StorageReference) {
        data?.let {
            val repo: ThreadRepository by inject()
            val upload = UrlUpload(riversRef.toString())
            CoroutineScope(IO).launch {
                if (it.jenis == UPLOAD_IMAGE) {
                    val respon =  async {
                        repo.newImage(it.idThread, upload)
                    }.await()
                    withContext(Main) {
                        if (respon.isSuccessful) {
                            deliverResultToReceiver(
                                SUCCESS_RESULT_IMAGE,
                                UploadStatus(Success, null, "Upload", riversRef.toString())
                            )
                            displayNotification(
                                "Pemberitahuan Upload",
                                " Foto baru berhasil di tambahkan"
                            )
                        } else {
                            deliverResultToReceiver(
                                SUCCESS_RESULT_IMAGE,
                                UploadStatus(ErrorUpload, null, "Upload")
                            )
                            displayNotification(
                                "Pemberitahuan Upload",
                                "Gagal. Mohon upload kembali"
                            )
                        }
                    }

                }

                if (it.jenis == UPLOAD_VIDEO) {

                    val respon = async {
                        repo.newVideo(it.idThread, upload)
                    }.await()
                    withContext(Main) {
                        if (respon.isSuccessful) {
                            deliverResultToReceiver(
                                SUCCESS_RESULT_VIDEO,
                                UploadStatus(Success, null, "Upload", riversRef.toString())
                            )
                            displayNotification(
                                "Pemberitahuan Upload",
                                " Video baru berhasil di tambahkan"
                            )
                        } else {
                            deliverResultToReceiver(
                                SUCCESS_RESULT_VIDEO,
                                UploadStatus(ErrorUpload, null, "Upload")
                            )
                            displayNotification(
                                "Pemberitahuan Upload",
                                "Gagal. Mohon upload kembali"
                            )
                        }
                    }

                }


            }
        }
    }


    private fun uploadImage(
        idThread: String?,
        data: NewFile?
    ) {
        val storage = FirebaseStorage.getInstance()
        data?.let { data_ ->

            val file = Uri.fromFile(File(data_.path))
            val riversRef = storage.reference.child("post/${UUID.randomUUID()}")
            val uploadTask = riversRef.putFile(file)
            uploadTask.addOnProgressListener {
                val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
                displayNotificationProgress(progression = progress.toInt())
                deliverResultToReceiver(
                    SUCCESS_RESULT_IMAGE,
                    UploadStatus(InProgress, progress.toInt(), "Sedang Upload")
                )
            }.addOnSuccessListener {
                cancelNotif(NotificationID.N_IDPROGRESSUPLOAD)
                uploadToNodeServer(idThread, data, riversRef)
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