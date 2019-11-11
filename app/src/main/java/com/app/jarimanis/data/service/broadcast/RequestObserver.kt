package com.app.jarimanis.data.service.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.jarimanis.data.datasource.models.thread.Status
import com.app.jarimanis.data.datasource.models.thread.UploadStatus



//tahap build
class RequestObserver(
    private val context: Context,
    private val delegate: RequestObserverDelegate
) : BroadcastReceiver(), LifecycleObserver {

    private var subscribedUploadID: String? = null

    init {
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val safeIntent = intent ?: return
//        val data = intent.getParcelableExtra<UploadStatus>(STATUS_UPLOAD_DATA)
//
//
//
//        when (data.status) {
//            Status.InProgress -> delegate.onProgress(context, uploadInfo)
//            Status.ErrorUpload -> delegate.onError(context, uploadInfo, data.exception!!)
//            Status.Success -> delegate.onSuccess(context, uploadInfo, data.serverResponse!!)

//        }
    }

    /**
     * Method called every time a new event arrives from an upload task, to decide whether or not
     * to process it. If this request observer subscribed a particular upload task, it will listen
     * only to it
     *
     * @param uploadInfo upload info to
     * @return true to accept the event, false to discard it
     */


    /**
     * Register this upload receiver to listen for events.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun register() {
//        LocalBroadcastManager.getInstance(context)
//            .registerReceiver(this, UploadServiceConfig.broadcastStatusIntentFilter)
//
//        subscribedUploadID?.let {
//            if (!UploadService.taskList.contains(it)) {
//                delegate.onCompletedWhileNotObserving()
//            }
//        }
    }

    /**
     * Unregister this upload receiver from listening events.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregister() {
//        LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
    }

    /**
     * Subscribe to get only the events from the given upload request. Otherwise, it will listen to
     * all the upload requests.
     */
//    fun subscribe(request: UploadRequest<*>) {
//        subscribedUploadID = request.startUpload()
//    }
}