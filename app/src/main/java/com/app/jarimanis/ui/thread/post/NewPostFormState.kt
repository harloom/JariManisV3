package com.app.jarimanis.ui.thread.post

data class NewPostFormState (
    val titleError: Int? = null,
    val contentError: Int? = null,
    val isDataValid: Boolean = false
)