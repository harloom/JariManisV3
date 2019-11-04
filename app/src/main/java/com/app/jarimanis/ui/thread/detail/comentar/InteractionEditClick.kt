package com.app.jarimanis.ui.thread.detail.comentar

import com.app.jarimanis.data.datasource.models.diskusi.paging.Doc


interface InteractionEditClick {
    fun onDeleteListener(
        item: Doc,
        position: Int
    )
    fun onEditListerner(id : String , content : String?)
    fun onLaporListener(item: Doc)
}