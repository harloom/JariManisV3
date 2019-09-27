package com.app.jarimanis.ui.thread.post


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log

import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.*

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori
import com.app.jarimanis.data.datasource.models.thread.ImageX
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import com.app.jarimanis.data.datasource.models.thread.VideoX
import com.app.jarimanis.utils.G4Engine
import com.app.jarimanis.utils.GifSizeFilter
import com.app.jarimanis.utils.Key
import com.app.jarimanis.utils.RequestCode.REQUEST_CODE_CAMERA
import com.app.jarimanis.utils.RequestCode.REQUEST_CODE_IMAGE
import com.app.jarimanis.utils.RequestCode.REQUEST_CODE_RESULT_TRIM
import com.app.jarimanis.utils.RequestCode.REQUEST_CODE_VIDEO
import com.app.jarimanis.utils.afterTextChanged
import com.app.jarimanis.utils.trimVideo.TrimmerActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yalantis.ucrop.UCrop.EXTRA_INPUT_URI
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import kotlinx.android.synthetic.main.create_thread_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class CreateThreadFragment : Fragment(), ImageStringAdapter.Interaction {
    override fun onItemSelected(position: Int, item: String) {

    }



    companion object {
        fun newInstance() = CreateThreadFragment()
    }
    private lateinit var category :String
    private var jobChangeText : Job? = null

    private  val vm: CreateThreadViewModel by viewModel()
    private lateinit var adapterI : ImageStringAdapter
    private enum class VolumeState { ON, OFF }
    private var mSelected: MutableList<String> = mutableListOf()
    private var videosSelected : String = ""
    private lateinit var videoSurfaceView: PlayerView
    private  var videoPlayer: SimpleExoPlayer? =null
    private lateinit var volumeControl: ImageView
    private  var volumeState: VolumeState? = null
    private lateinit var requestManager: RequestManager

    private var btnSendEnable by Delegates.observable(false){
        _,old,new ->
        if(new != old){
            activity?.invalidateOptionsMenu()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_thread_fragment, container, false)
    }

    private lateinit var uploadWorkRequest: WorkRequest

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val result  = arguments?.getParcelable<ResultKategori>(Key.argCategory)
        category = result?.category!!
        activity!!.title = result.category

        videoSurfaceView = view.findViewById(R.id.player_view)
        requestManager = initGlide()
        adapterI = ImageStringAdapter(this@CreateThreadFragment)
        subcribeForm()
        subcribeFormState()
        btn_requestfile.setOnClickListener{
            chooseImageDialog()
        }
    }

    private fun subcribeWorkUpload() {
        WorkManager.getInstance(context!!).getWorkInfoByIdLiveData(uploadWorkRequest.id).observe(
            this@CreateThreadFragment, Observer {workInfo->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    displayMessage("Work finished!")
                }
            }
        )
    }

    private fun displayMessage(a: String) {
        Toast.makeText(context,"Work : $a" , Toast.LENGTH_LONG).show()
    }

    private fun subcribeForm() {
        post_content.afterTextChanged{
            jobChangeText?.cancel()
            jobChangeText = CoroutineScope(Main).launch {
                delay(500)
                vm.formPostDataChange(
                    PostValidationModel(title = post_titile.text.toString(),content = post_content.text.toString())
                )
            }
        }

        post_titile.afterTextChanged {
            jobChangeText?.cancel()
            jobChangeText = CoroutineScope(Main).launch {
                delay(500)
                vm.formPostDataChange(
                    PostValidationModel(title = post_titile.text.toString(),content = post_content.text.toString())
                )
            }
        }
    }

    private fun subcribeFormState() {
        vm.newPostState.observe(this@CreateThreadFragment, Observer {
            val newPost = it ?: return@Observer
            btnSendEnable = it.isDataValid
            if (newPost.titleError != null) {
                post_titile.error = getString(newPost.titleError)

            }
            if (newPost.contentError != null) {
                post_content.error = getString(newPost.contentError)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_post, menu)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
       val btn =  menu.findItem(R.id.optionMenuSend)
           btn.isEnabled = btnSendEnable

    }

    private var jobSent : Job? = null
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.optionMenuSend->{
                jobSent?.cancel()
                jobSent = CoroutineScope(Main).launch {
                    delay(200)
                    onSendUpload()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun onSendUpload(){
        val imageListX : MutableList<ImageX> = mutableListOf()
        val videoListX : MutableList<VideoX> = mutableListOf()
        videoListX.add(VideoX(videosSelected))
        mSelected.map {
            imageListX.add(ImageX(it))
        }
        val upload = UploadThread(category,post_content.text.toString(),imageListX,post_titile.text.toString(),
            videoListX )


//        uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWork>()
//            .setInputData(dataWorker)
//            .build()
        vm.post(upload)
//        subcribeWorkUpload()
    }


    private fun chooseImageDialog() {
        val dialogSheet = BottomSheetDialog(context!!)
        val layout: View = layoutInflater.inflate(R.layout.dialog_image_picker, null)
        dialogSheet.setContentView(layout)
        dialogSheet.setCancelable(false)
        dialogSheet.show()
        val dismiss = layout.findViewById<TextView>(R.id.choosePhotoClose)
        val pickGallery = layout.findViewById<LinearLayout>(R.id.pick_gallery)
        val pickCamera = layout.findViewById<LinearLayout>(R.id.pick_camera)
        val pickVideo  = layout.findViewById<LinearLayout>(R.id.pick_video)
        dismiss.setOnClickListener {
            dialogSheet.dismiss()
        }

        pickCamera.setOnClickListener {
            loadImageFromCammera()
            dialogSheet.dismiss()
        }
        pickGallery.setOnClickListener {
            loadImageFromGallery()
            dialogSheet.dismiss()
        }
        pickVideo.setOnClickListener {
            loadVideoFromGallery()
            dialogSheet.dismiss()
        }

    }
    private fun loadImageFromCammera(){
        ImagePicker.with(this@CreateThreadFragment)
            .cameraOnly()    //User can only select image from Gallery
            .cropSquare()
            .compress(1024)
            .start(REQUEST_CODE_CAMERA)
    }
    private fun loadImageFromGallery() {
        Matisse.from(this@CreateThreadFragment)
            .choose(MimeType.ofImage())
            .countable(true)
            .showSingleMediaType(true)
            .maxSelectable(3)
            .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(G4Engine())
            .forResult(REQUEST_CODE_IMAGE)
    }

    private fun loadVideoFromGallery() {
        Matisse.from(this@CreateThreadFragment)
            .choose(MimeType.ofVideo())
            .showSingleMediaType(true)
            .countable(true)
            .maxSelectable(1)
            .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(G4Engine())
            .forResult(REQUEST_CODE_VIDEO)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            Log.d("Matisseiilham", "Uris: " + Matisse.obtainResult(data));
            Log.d("Matisseiilham", "Paths: " + Matisse.obtainPathResult(data));
            mSelected = Matisse.obtainPathResult(data)
            adapterI.submitList(mSelected)
            rcv_foto.adapter = adapterI
            destroyVideo()
        } else if (requestCode == REQUEST_CODE_VIDEO && resultCode == Activity.RESULT_OK) {
            Log.d("MatisseiVideo", "Uris: " + Matisse.obtainResult(data));
            Log.d("MatisseiVideo", "Paths: " + Matisse.obtainPathResult(data))
            startTrimActivity(Matisse.obtainResult(data)[0])
        } else if (requestCode == REQUEST_CODE_RESULT_TRIM && resultCode == Activity.RESULT_OK) {
            Log.d("TriMVideo", "Uri: " + data?.extras!!["returnUri"])
            val uri  = data.extras!!["returnUri"] as Uri
            initVideo(uri)
        }else if ( requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK){
            val filePath:String = ImagePicker.getFilePath(data).toString()
            mSelected.add(filePath)
            adapterI.submitList(mSelected)
            rcv_foto.adapter = adapterI
        }
    }
    private fun startTrimActivity(uri: Uri) {
        val intent = Intent(context, TrimmerActivity::class.java)
        intent.putExtra(EXTRA_INPUT_URI, uri)
        startActivityForResult(intent, REQUEST_CODE_RESULT_TRIM)
    }


    private fun initVideo(uri : Uri) {
        videoSurfaceView.visibility = View.VISIBLE
        videosSelected = uri.toString()
        val bandwidthMeter = DefaultBandwidthMeter.Builder(context)
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory();
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory);
        videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        // Bind the player to the view.
        videoSurfaceView.useController = true
        videoSurfaceView.player = videoPlayer
        volumeControl = view!!.findViewById(R.id.exo_volume)
        volumeControl.setOnClickListener {
            toggleVolume()
        }
        setVolumeControl(VolumeState.ON)

        val defaultDataSource: DataSource.Factory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, resources.getString(R.string.app_name))
        )
        val videoSource: MediaSource = ProgressiveMediaSource.Factory(defaultDataSource)
            .createMediaSource(uri)
        videoPlayer?.prepare(videoSource)
        videoPlayer?.playWhenReady = false



//        videoPlayer.addListener(this)

    }

    private fun startPlauyer() {
        videoPlayer.let {
            it?.playWhenReady = true
            it?.playbackState
        }

    }

    private fun pausePlayer() {
        videoPlayer.let {
            it?.playWhenReady = false
            it?.playbackState
        }

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
    private fun destroyVideo(){
        videosSelected = ""
        videoSurfaceView.player = null
        videoPlayer?.release()
        videoSurfaceView.visibility = View.GONE
    }
    private fun initGlide()
            : RequestManager {
        val optios = RequestOptions().placeholder(R.drawable.white_background)
            .error(R.drawable.white_background)

        return Glide.with(this).setDefaultRequestOptions(optios)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        if(videoPlayer !=null){
            destroyVideo()
        }
    }



}
