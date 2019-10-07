package com.app.jarimanis.ui.chat

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.chats.Result
import com.bumptech.glide.Glide
import com.snov.timeagolibrary.PrettyTimeAgo
import kotlinx.android.synthetic.main.item_chat_user.view.*

class ChatChannelListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return  oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ChannelHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chat_user,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChannelHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }



    fun submitList(list: List<Result?>) {
        differ.submitList(list)

    }


    class ChannelHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Result) = with(itemView) {

            val channel = item.channelID
            val userList     = channel?.userList
            userList?.map { user->
                if(user?.user?.id != TokenUser.idUser){
                    println("Glide Enemy")
                    try {
                        itemView.setOnClickListener {
                            interaction?.onItemSelected(adapterPosition, item)
                        }


                        tv_time.text = PrettyTimeAgo.getTimeAgo(channel.updateAt!!)
                        tv_user.text  = user?.user?.nameUser
                        Glide.with(itemView.context).load(user?.user?.thumbail).into(cv_thumbail)
                    }catch (e : Exception){
                        println("Error Glide : ${e}")
                    }
                }
            }

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Result)
    }
}
