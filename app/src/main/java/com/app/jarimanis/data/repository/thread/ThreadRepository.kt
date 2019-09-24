package com.app.jarimanis.data.repository.thread

import androidx.lifecycle.LiveData

interface ThreadRepository {
    fun getProductPaging(subId : String ,page: String) : LiveData<Threads>

}