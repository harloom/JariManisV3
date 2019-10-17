package com.app.jarimanis.ui.thread.detail

import androidx.recyclerview.widget.RecyclerView
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
import com.bumptech.glide.RequestManager
import android.R.attr.thumbnail
import android.view.View
import androidx.annotation.NonNull
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.FrameLayout
import android.widget.ImageView
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.Video

/* progress BUILDING */
class VideoPlayerViewHolder constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private  val media_container: FrameLayout

     val thumbnail: ImageView
     val volumeControl: ImageView
     val progressBar: ProgressBar
     lateinit var requestManager: RequestManager

    init {
        media_container = itemView.findViewById(R.id.media_container)
        thumbnail = itemView.findViewById(R.id.thumbnail)
        progressBar = itemView.findViewById(R.id.progressBar)
        volumeControl = itemView.findViewById(R.id.volume_control)
    }

    fun onBind(mediaObject: Video, requestManager: RequestManager) {
        this.requestManager = requestManager
//        parent.setTag(this)
//        title.setText(mediaObject.g())
//        this.requestManager
//            .load(mediaObject.getThumbnail())
//            .into(thumbnail)
    }

}
