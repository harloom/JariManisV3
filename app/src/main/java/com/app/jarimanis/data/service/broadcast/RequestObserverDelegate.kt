package com.app.jarimanis.data.service.broadcast

import android.content.Context

interface RequestObserverDelegate {
    fun onProgress(context: Context, progress : Float)

    /**
     * Called when the upload is completed successfully.
     *
     * @param context context
     * @param uploadInfo upload status information
     * @param serverResponse response got from the server
     */
    fun onSuccess(context: Context, message : String)

    /**
     * Called when an error happens during the upload.
     *
     * @param context context
     * @param uploadInfo upload status information
     * @param exception exception that caused the error
     */
    fun onError(context: Context,  message : String, exception: Throwable)

    /**
     * Called when the upload is completed wither with success or error.
     *
     * @param context context
     * @param uploadInfo upload status information
     */
    fun onCompleted(context: Context, message : String)


    fun onCompletedWhileNotObserving()
}