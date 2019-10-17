package com.app.jarimanis.ui.thread.detail

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.Video
import com.app.jarimanis.ui.thread.post.CreateThreadFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_video.view.*

class VideoListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Video>() {

        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
         return  oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    private val ActivityActive  : LiveData<Boolean> = MutableLiveData<Boolean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return VideoHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_video,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideoHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

    }
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        when(holder){
            is VideoHolder -> {
                holder.pausePlayer()
                holder.stopPlayer()
            }
        }
    }
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

    }

    fun submitList(list: List<Video?>) {
        differ.submitList(list)
    }

    class VideoHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        private enum class VolumeState { ON, OFF }
        private lateinit var videoSurfaceView: PlayerView
        private  var videoPlayer: SimpleExoPlayer? =null
        private lateinit var volumeControl: ImageView
        private  var volumeState: VolumeState? = null
        private lateinit var requestManager: RequestManager
        fun bind(item: Video) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            requestManager = initGlide()
            videoSurfaceView = player_view
            try {
              item.url?.let {

                  val gsReference = FirebaseStorage.getInstance()
                      .getReferenceFromUrl(item.url)
                  gsReference.downloadUrl.addOnSuccessListener {uri->
                      initVideo(uri)
                  }

              }
            }catch (e : Exception){
                println("Video Error : ${e.message}")
            }


        }


        private fun initVideo(uri : Uri) {
            videoSurfaceView.visibility = View.VISIBLE

            val bandwidthMeter = DefaultBandwidthMeter.Builder(itemView.context)
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory();
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory);
            videoPlayer = ExoPlayerFactory.newSimpleInstance(itemView.context, trackSelector);
            // Bind the player to the view.
            videoSurfaceView.useController = true
            videoSurfaceView.player = videoPlayer
            volumeControl = itemView.findViewById(R.id.exo_volume)
            volumeControl.setOnClickListener {
                toggleVolume()
            }
            setVolumeControl(VolumeState.ON)

            val defaultDataSource: DataSource.Factory = DefaultDataSourceFactory(
                itemView.context,
                Util.getUserAgent(   itemView.context,  itemView.context.resources.getString(R.string.app_name))
            )
            val videoSource: MediaSource = ProgressiveMediaSource.Factory(defaultDataSource)
                .createMediaSource(uri)
            videoPlayer?.prepare(videoSource)
            videoPlayer?.playWhenReady = false


        }


        private fun setVolumeControl(state: VolumeState) {
            volumeState = state
            if (state == VolumeState.OFF) {
                videoPlayer?.volume = 0f;
                animateVolumeControl();
            } else if (state == VolumeState.ON) {
                videoPlayer?.volume = 1f;
                animateVolumeControl();
            }
        }

        private fun animateVolumeControl() {
            if (volumeControl != null) {
                volumeControl.bringToFront()
                if (volumeState == VolumeState.OFF) {
                    requestManager.load(R.drawable.ic_volume_off_grey_24dp)
                        .into(volumeControl)
                } else if (volumeState == VolumeState.ON) {
                    requestManager.load(R.drawable.ic_volume_up_grey_24dp)
                        .into(volumeControl)
                }
                volumeControl.animate().cancel()

                volumeControl.alpha = 1f

                volumeControl.animate()
                    .alpha(1f)
                    .setDuration(600).startDelay = 1000
            }
        }

        private fun toggleVolume() {
            if (videoPlayer != null) {
                if (volumeState == VolumeState.OFF) {

                    setVolumeControl(VolumeState.ON)

                } else if(volumeState == VolumeState.ON) {

                    setVolumeControl(VolumeState.OFF)

                }
            }
        }
        private fun startPlauyer() {
            videoPlayer.let {
                it?.playWhenReady = true
                it?.playbackState
            }

        }

         fun pausePlayer() {
            videoPlayer.let {
                it?.playWhenReady = false
                it?.playbackState
            }

        }

          fun  stopPlayer(){
            videoPlayer!!.release()

        }

        private fun initGlide()
                : RequestManager {
            val optios = RequestOptions().placeholder(R.drawable.white_background)
                .error(R.drawable.white_background)

            return Glide.with(itemView.context).setDefaultRequestOptions(optios)
        }

    }




    interface Interaction {
        fun onItemSelected(position: Int, item: Video)
    }
}
