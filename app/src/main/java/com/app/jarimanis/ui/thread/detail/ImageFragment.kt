package com.app.jarimanis.ui.thread.detail

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.datasource.models.thread.Image
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.data.service.NewFileService
import com.app.jarimanis.data.service.NewFileService.Companion.RECEIVER_NEWFILE
import com.app.jarimanis.data.service.result_reciver.UploadReciver
import com.app.jarimanis.ui.photoView.ARG_PARAM1
import com.app.jarimanis.utils.G4Engine
import com.app.jarimanis.utils.GifSizeFilter
import com.app.jarimanis.utils.Key
import com.app.jarimanis.utils.RequestCode
import com.app.jarimanis.utils.RequestCode.REQUEST_CODE_IMAGE
import com.app.jarimanis.utils.debounce.onClickDebounced
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.Timestamp
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import kotlinx.android.synthetic.main.detail_thread_image_fragment.*
import kotlinx.android.synthetic.main.detail_thread_image_fragment.refress
import kotlinx.android.synthetic.main.thread_list_fragment.*

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import net.alhazmy13.gota.Gota
import net.alhazmy13.gota.GotaResponse
import org.koin.android.ext.android.inject
import java.net.URI
import java.util.*
import kotlin.system.measureTimeMillis

class ImageFragment : Fragment(), ImageAdapter.Interaction, Gota.OnRequestPermissionsBack {
    override fun onRequestBack(requestId: Int, gotaResponse: GotaResponse) {
        if(gotaResponse.isAllGranted) {
            btn_newFoto.onClickDebounced {
                loadImageFromGallery()
            }
        }
    }


    override fun onItemSelected(position: Int, item: Image, uri: String) {
        val bundleof = bundleOf(ARG_PARAM1 to uri )
        try {
            findNavController().navigate(R.id.photoDetail,bundleof)
        }catch (e  : Exception){

        }
    }


    companion object {
        const val JOB_TIME_OUT = 5000L
        fun newInstance() = ImageFragment()
    }

    private lateinit var imageListAdapter: ImageAdapter
    private  var mutableList: MutableList<Image?>? = mutableListOf()
    private  var doc : Doc? =null

    private lateinit var mShimmer : ShimmerFrameLayout
    private lateinit var rcvImage  : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_thread_image_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _id  = arguments?.getString(Key.THREADID)
        mShimmer = view.findViewById(R.id.shimmer_image)
        rcvImage = view.findViewById(R.id.rcv_thumbnails)
        startAnimation()
        doc = arguments?.getParcelable<Doc>(Key.THREAD)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        doc?.let {
            initUi(it)
            cekPremission(it)
            onRefress()
        }
    }

    private fun initUi(it: Doc) {
        val repo: ThreadRepository by inject()
        CoroutineScope(IO).launch {
            val job = withTimeoutOrNull(JOB_TIME_OUT) {
                val res = async {
                    repo.getImageList(it.id!!)
                } .await()
                if(res.isSuccessful){
                    stopAnimation()
                    sentToRecyleView(it,res.body())
                }

            }

            if(job == null){
                withContext(Main){
                    Toast.makeText(context,"Request Time Out!!, Please Try Again",Toast.LENGTH_LONG).show()
                }
            }

        }




    }

    suspend fun sentToRecyleView(dc :Doc,images: List<Image>?) {
        withContext(Main){
            imageListAdapter = ImageAdapter(dc.id!!,this@ImageFragment)
            mutableList = images?.toMutableList()
            imageListAdapter.submitList(mutableList)
            rcvImage.apply {
                adapter = imageListAdapter

            }
        }

    }


    private fun cekPremission(item : Doc){
        Gota.Builder(activity).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA).setListener(this).check()
        if(item.user?.id != TokenUser.idUser){
            btn_newFoto.visibility = View.GONE
        }


    }

    private fun loadImageFromGallery() {
            Matisse.from(this@ImageFragment)
                .choose(MimeType.ofImage())
                .countable(true)
                .showSingleMediaType(true)
                .maxSelectable(1)
                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(G4Engine())
                .forResult(RequestCode.REQUEST_CODE_IMAGE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
           val  mSelected = Matisse.obtainPathResult(data)
            mSelected.forEach {
                val mImage = Image(Timestamp.now().toString(),"local "+UUID.randomUUID().toString(),Timestamp.now().toString(),it)
                mutableList!!.add(mImage)
            }
            imageListAdapter.submitList(mutableList)
            imageListAdapter.notifyDataSetChanged()

        }
    }



    private var jobRefress : Job? =null
    private fun onRefress() {
        refress.setOnRefreshListener {
            startAnimation()
            jobRefress?.cancel()
            doc?.let { initUi(it) }
            refress.isRefreshing = false
        }
    }

    private fun stopAnimation() {
        try{
            CoroutineScope(Main).launch {
                mShimmer.stopShimmerAnimation()
                rcvImage.visibility = View.VISIBLE
                mShimmer.visibility = View.GONE
                delay(500)
            }
        }catch (e : Exception){

        }


    }

    private fun startAnimation() {
        try {
            CoroutineScope(Main).launch {
                rcvImage.visibility = View.INVISIBLE
                mShimmer.visibility = View.VISIBLE
                mShimmer.startShimmerAnimation()
                delay(2000)
            }

        }catch (e: Exception){

        }

    }
}