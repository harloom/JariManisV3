package com.app.jarimanis.ui.thread.detail.holder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.jarimanis.data.datasource.models.thread.Image
import com.app.jarimanis.data.datasource.models.thread.Status.Success
import com.app.jarimanis.data.datasource.models.thread.UploadStatus
import com.app.jarimanis.data.service.NewFileService
import com.app.jarimanis.data.service.NewFileService.Companion.SUCCESS_RESULT
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
        if (resultCode == SUCCESS_RESULT) {
            val data = resultData?.getParcelable<UploadStatus>(NewFileService.RESULT_DATA_KEY)
            loadUi(data)
        }

    }

    private lateinit var resultReceiver: UploadReciver
    private lateinit var mItem: Image
    fun bind(item: Image) = with(itemView) {
        resultReceiver = UploadReciver(Handler())
        resultReceiver.set(this@ImageLocalHolder)
        startIntentService(item.url!!)

    }

    private fun loadUi(data: UploadStatus?) {
        try {
            itemView.iv_clear.visibility = View.GONE
            if (data?.status == Success) {
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
            }


        } catch (e: Exception) {

        }
    }

    private fun startIntentService(it: String) {
        val intent = Intent(itemView.context, NewFileService::class.java).apply {
            putExtra(NewFileService.RECEIVER_NEWFILE, resultReceiver)
//            putExtra(NewFileService.RESULT_DATA_KEY, it)
            putExtra(NewFileService.KEY_NEWFILE,it)
//            error disini
        }
        NewFileService().enqueueWork(itemView.context,intent)


    }

}