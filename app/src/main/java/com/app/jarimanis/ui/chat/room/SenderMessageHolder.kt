package com.app.jarimanis.ui.chat.room

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.message.ReciveMessage
import com.app.jarimanis.utils.SendStatus.PENDING
import com.app.jarimanis.utils.TimeFormater
import kotlinx.android.synthetic.main.item_message_sender.view.*

class SenderMessageHolder
  constructor(itemView: View,
              private val interaction: Interaction?
  ) : RecyclerView.ViewHolder(itemView) {

            fun bind(item: ReciveMessage) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
                try {
                    text_message_body.text = item._message
                    text_message_time.text = TimeFormater.parse(item.timestamp?.toDate()!!)
                    msg_status.setImageResource(R.drawable.ic_done)
                    if(item.status !=null){
                        when(item.status){
                            PENDING->{
                                msg_status.setImageResource(R.drawable.ic_schedule)
                            }else->{
                                msg_status.setImageResource(R.drawable.ic_done)
                            }
                        }
                    }
                }catch (e : Exception){
                    println("Error Sender Message : ${e.toString()}")
                }

        }
    }




