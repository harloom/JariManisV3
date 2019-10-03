package com.app.jarimanis.ui.chat.room

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.jarimanis.data.datasource.models.chats.User
import com.app.jarimanis.data.datasource.models.message.ReciveMessage
import com.app.jarimanis.utils.TimeFormater
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_message_received.view.*


class ReciveMessageHolder(
    itemView: View,
    private val interaction: Interaction?
) : RecyclerView.ViewHolder(itemView) {

            fun bind(item: ReciveMessage , user: User) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
                try {
                    text_message_name.text = user.user?.nameUser
                    text_message_body.text = item._message
                    text_message_time.text = TimeFormater.parse(item.timestamp?.toDate()!!)
                    Glide.with(itemView.context).load(user.user?.thumbail).into(image_message_profile)
                }catch (e : Exception){
                    println("Error ReciveMessage : ${e.toString()}")
                }


        }
    }



