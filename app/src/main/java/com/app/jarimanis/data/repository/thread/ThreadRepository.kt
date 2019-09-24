package com.app.jarimanis.data.repository.thread

import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.thread.Threads

interface ThreadRepository {
    fun getProductPaging(subId : String ,page: String) : LiveData<Threads>

}