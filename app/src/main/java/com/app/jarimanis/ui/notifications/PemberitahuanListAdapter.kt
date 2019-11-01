package com.app.jarimanis.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.pemberitahuan.Doc
import com.bumptech.glide.Glide
import com.snov.timeagolibrary.PrettyTimeAgo
import kotlinx.android.synthetic.main.item_notification.view.*


class PemberitahuanListAdapter(private val interaction: Interaction? = null)  : PagedListAdapter<Doc, RecyclerView.ViewHolder>(
object  :  DiffUtil.ItemCallback<Doc>(){
    override fun areItemsTheSame(oldItem: Doc, newItem: Doc): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Doc, newItem: Doc): Boolean {
        return  oldItem == newItem
    }

}
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Pemberitahuan1Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_notification,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

            when (holder) {
                is Pemberitahuan1Holder -> {
                    item?.let {doc->
                        holder.bind(doc)

                }
            }
        }

    }


    inner class Pemberitahuan1Holder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Doc) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            try {
                tv_content.text = item.message
                Glide.with(itemView.context).load(item._fromId?.thumbail).into(cv_thumbail)
                tv_time.text = PrettyTimeAgo.getTimeAgo(item.updateAt!!)
//                if(!item.read!!){
//                    card_backgorund.setCardBackgroundColor(ContextCompat.getColor(itemView.context,R.color.colorBgGrey))
//                }else{
//                    card_backgorund.setCardBackgroundColor(ContextCompat.getColor(itemView.context,R.color.colorPutih))
//                }
            }catch (e : Exception){
                Toast.makeText(itemView.context,"Something Error , ${e.message}",Toast.LENGTH_SHORT).show()
            }



        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Doc)
    }

}






