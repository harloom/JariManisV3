package com.app.jarimanis.ui.thread

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_thread.view.*
import java.lang.Exception


class ThreadAdapter constructor(private val interaction: ThreadAdapter.Interaction? = null) : PagedListAdapter<Doc, RecyclerView.ViewHolder>(
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
        return ThreadHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_thread,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        println(position)
        when(holder){
            is ThreadHolder ->{
                item?.let {
                    holder.bind(it)
                }
            }
        }

    }


    class ThreadHolder(
        view: View,
        private val interaction: ThreadAdapter.Interaction?
    ) : RecyclerView.ViewHolder(view) {

        fun bind(item: Doc) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition,item)
            }

            itemView.tv_time.text = item.updateAt
            itemView.tv_judul.text = item.title?.capitalize()
            itemView.tv_user.text = item.user?.nameUser?.capitalize()

            try {
                Glide.with(itemView.context).load(item.user?.thumbail)
                    .into(itemView.cv_thumbail)
            }catch (e : Exception){

            }

        }
    }
    interface Interaction {
        fun onItemSelected(position: Int, item: Doc)
    }
}