package com.app.jarimanis.ui.thread.detail.holder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.jarimanis.data.datasource.models.thread.Image
import com.app.jarimanis.data.datasource.models.thread.NewFile
import com.app.jarimanis.data.datasource.models.thread.Status.Success
import com.app.jarimanis.data.datasource.models.thread.UploadStatus
import com.app.jarimanis.data.service.NewFileService
import com.app.jarimanis.data.service.NewFileService.Companion.KEY_THREADID
import com.app.jarimanis.data.service.NewFileService.Companion.RESULT_DATA_KEY
import com.app.jarimanis.data.service.NewFileService.Companion.SUCCESS_RESULT_IMAGE
import com.app.jarimanis.data.service.NewFileService.Companion.UPLOAD_IMAGE
import com.app.jarimanis.data.service.result_reciver.UploadReciver
import com.app.jarimanis.ui.thread.detail.ImageAdapter
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_image.view.*

class ImageLocalHolder constructor(
    itemView: View,
    private val interaction: ImageAdapter.Interaction?
) : RecyclerView.ViewHolder(itemView), UploadReciver.UploadReciverInterface {
    override fun onRecive(resultCode: Int, resultData: Bundle?) {
        if (resultCode == SUCCESS_RESULT_IMAGE) {
            val data = resultData?.getParcelable<UploadStatus>(RESULT_DATA_KEY)
            loadUi(data)
        }

    }

    private lateinit var resultReceiver: UploadReciver
    private lateinit var mItem: Image
    fun bind(item: Image, idThread: String) = with(itemView) {
        resultReceiver = UploadReciver(Handler())
        resultReceiver.set(this@ImageLocalHolder)
        startIntentService(item , idThread)
        itemView.image_progress.visibility = View.VISIBLE

    }

    private fun loadUi(data: UploadStatus?) {
        try {

            itemView.iv_clear.visibility = View.GONE
            if (data?.status == Success) {
                mItem = Image("example",System.currentTimeMillis().toString(),"example",data.storage.toString() )
                itemView.image_progress.visibility = View.GONE
                val gsReference =
                    FirebaseStorage.getInstance().getReferenceFromUrl(data.storage.toString())
                     gsReference.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(itemView.context).load(uri).into(itemView.iv_img)
                    itemView.setOnClickListener {
                        interaction?.onItemSelected(adapterPosition, mItem, uri.toString())
                    }
                }.addOnFailureListener {
                    print("Error Glide $it")
                }
            }else{

                /* handle Error */
            }


        } catch (e: Exception) {

        }
    }

    private fun startIntentService(
        it: Image,
        idThread: String
    ) {
        val data = NewFile(idThread,UPLOAD_IMAGE,it.url!!)
        val intent = Intent(itemView.context, NewFileService::class.java).apply {
            putExtra(NewFileService.RECEIVER_NEWFILE, resultReceiver)
            putExtra(KEY_THREADID,idThread)
            putExtra(NewFileService.KEY_NEWFILE,data)
        }
        NewFileService().enqueueWork(itemView.context,intent)


    }

}