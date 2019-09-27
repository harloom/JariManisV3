package com.app.jarimanis.ui.thread.post

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.data.service.PostIntentService


class CreateThreadViewModel(private  val context : Context , private val repo: ThreadRepository) : ViewModel() {
    private val newPostForm = MutableLiveData<NewPostFormState>()
    val newPostState: LiveData<NewPostFormState> = newPostForm

    private val postResult = MutableLiveData<PostResult>()
    val _postResult: LiveData<PostResult> = postResult

    private val _senServer = MutableLiveData<UploadThread>()

    fun post(post:UploadThread ) {
        val intentService  = Intent(context,PostIntentService::class.java)
        intentService.putExtra("upload",post)
        PostIntentService(repo).enqueueWork(context,intentService)
//        context.startService(intentService)
        resetForm()

    }
    fun formPostDataChange(post: PostValidationModel?) {
        post?.let {
            if (!isEmpity(it.title!!)) {
                newPostForm.value = NewPostFormState(titleError = R.string.fromEmpty)
            } else
                if (!isEmpity(it.content!!)) {
                    newPostForm.value = NewPostFormState(contentError = R.string.fromEmpty)
                } else {
                    newPostForm.value = NewPostFormState(isDataValid = true)
                }
        }
    }

    fun resetForm() {
        postResult.value = PostResult(success = "Ok")
    }

    private fun isEmpity(string: String): Boolean {
        return string.isNotEmpty()
    }
}
