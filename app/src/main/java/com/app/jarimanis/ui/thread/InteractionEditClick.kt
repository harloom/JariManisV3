package com.app.jarimanis.ui.thread

import com.app.jarimanis.data.datasource.models.thread.Doc

interface InteractionEditClick {
    fun onDeleteListener(item : Doc)
    fun onEditListerner(id : String,title : String? , content : String?)
    fun onLaporListener(item: Doc)
}