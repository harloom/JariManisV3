package com.app.jarimanis.data.service.result_reciver

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

class UploadReciver(handler: Handler?) : ResultReceiver(handler) {
    private  var mReceiver: UploadReciverInterface? =null

    fun set(reciverInterface: UploadReciverInterface){
        mReceiver = reciverInterface
    }


    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        super.onReceiveResult(resultCode, resultData)
        mReceiver?.onRecive(resultCode,resultData)
    }

      interface  UploadReciverInterface{
        fun onRecive(resultCode: Int,resultData: Bundle?)

    }

}