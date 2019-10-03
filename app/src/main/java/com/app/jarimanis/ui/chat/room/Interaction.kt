package com.app.jarimanis.ui.chat.room

import com.app.jarimanis.data.datasource.models.message.ReciveMessage

interface Interaction {
    fun onItemSelected(position: Int, item: ReciveMessage)

}
