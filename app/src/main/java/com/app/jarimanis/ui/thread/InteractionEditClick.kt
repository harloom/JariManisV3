package com.app.jarimanis.ui.thread

import com.app.jarimanis.data.datasource.models.thread.Doc

interface InteractionEditClick {
    fun onDeleteListener(item : Doc)
    fun onEditListerner(item: Doc)
    fun onLaporListener(item: Doc)
}