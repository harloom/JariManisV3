package com.app.jarimanis.ui.thread.detail

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.datasource.models.thread.Image
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
import com.google.firebase.Timestamp
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import kotlinx.android.synthetic.main.detail_thread_image_fragment.*
import kotlinx.android.synthetic.main.detail_thread_video_fragment.*
import java.net.URI
import java.util.*

class ImageFragment : Fragment(), ImageAdapter.Interaction {


    override fun onItemSelected(position: Int, item: Image, uri: String) {
        val bundleof = bundleOf(ARG_PARAM1 to uri )
        try {
            findNavController().navigate(R.id.photoDetail,bundleof)
        }catch (e  : Exception){

        }
    }


    companion object {
        fun newInstance() = ImageFragment()
    }

    private lateinit var imageListAdapter: ImageAdapter
    private  var mutableList: MutableList<Image?>? = mutableListOf()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_thread_image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _id  = arguments?.getString(Key.THREADID)
        val doc = arguments?.getParcelable<Doc>(Key.THREAD)

        doc?.let {
            initUi(it)
            cekPremission(it)
        }
    }

    private fun initUi(it: Doc) {
        imageListAdapter = ImageAdapter(this@ImageFragment)
        mutableList = it.images
        imageListAdapter.submitList(mutableList)
        rcv_thumbnails.apply {
            adapter = imageListAdapter

        }
    }


    private fun cekPremission(item : Doc){
        if(item.user?.id != TokenUser.idUser){
            btn_newFoto.visibility = View.GONE
        }else{
            btn_newFoto.onClickDebounced {
                loadImageFromGallery()
            }
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


}