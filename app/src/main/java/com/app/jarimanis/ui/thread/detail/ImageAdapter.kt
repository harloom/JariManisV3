package com.app.jarimanis.ui.thread.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.Image
import com.app.jarimanis.data.service.NewFileService
import com.app.jarimanis.data.service.result_reciver.UploadReciver
import com.app.jarimanis.ui.thread.detail.holder.ImageLocalHolder
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_image.view.*

class ImageAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Image>() {

        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0){
            return ImageHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_image,
                    parent,
                    false
                ),
                interaction
            )
        }else {
            return  ImageLocalHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_image,
                    parent,
                    false
                ),
                interaction
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageHolder -> {
                holder.bind(differ.currentList.get(position))
                println("imageHolder")
            }

            is ImageLocalHolder->{
                println("ImageLocalHolder")
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun submitList(list: List<Image?>?) {
        differ.submitList(list)
    }

    @Suppress("UNREACHABLE_CODE")
    override fun getItemViewType(position: Int): Int {
        val item  = differ.currentList[position]
        return if(item.id!!.contains("local ")){
            1
        }else{
            0
        }
    }

    class ImageHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {




        fun bind(item: Image) = with(itemView) {


            try {
                itemView.iv_clear.visibility = View.GONE
                val gsReference = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(item.url.toString())

                gsReference.downloadUrl.addOnSuccessListener {uri->
                    Glide.with(itemView.context).load(uri).into(iv_img)
                    itemView.setOnClickListener {
                        interaction?.onItemSelected(adapterPosition, item , uri.toString())
                    }
                }.addOnFailureListener{
                    print("Error Glide $it" )
                }

            }catch (e : Exception){

            }
        }




    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Image , uri : String)
    }
}
