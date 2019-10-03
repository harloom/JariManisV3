package com.app.jarimanis.ui.chat.room

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.chats.User
import com.app.jarimanis.data.datasource.models.message.ReciveMessage

@Suppress("UNREACHABLE_CODE")
class MessageAdapter(
    private val interaction: Interaction? = null,
    private val user: User
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private  val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2


    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReciveMessage>() {

        override fun areItemsTheSame(oldItem: ReciveMessage, newItem: ReciveMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReciveMessage, newItem: ReciveMessage): Boolean {
            return oldItem == newItem

        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType){
            VIEW_TYPE_MESSAGE_RECEIVED->{
                return ReciveMessageHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_message_received,
                        parent,
                        false
                    ),
                    interaction


                )
            }
            else->{
                return SenderMessageHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_message_sender,
                        parent,
                        false
                    ),
                    interaction
                )
            }

       }

    }

    override fun getItemViewType(position: Int): Int {
        val item : ReciveMessage = differ.currentList[position]
        return if(item._user == TokenUser.idUser){
            VIEW_TYPE_MESSAGE_SENT;
        }else{
            VIEW_TYPE_MESSAGE_RECEIVED
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReciveMessageHolder -> {
                holder.bind(differ.currentList.get(position),user)
            }
            is SenderMessageHolder->{
                holder.bind(differ.currentList.get(position))
            }else->{

        }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ReciveMessage>) {
        differ.submitList(list)
    }

}


