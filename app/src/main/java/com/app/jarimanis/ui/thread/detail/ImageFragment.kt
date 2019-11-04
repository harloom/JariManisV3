package com.app.jarimanis.ui.thread.detail

import android.net.Uri
import android.os.Bundle
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
import com.app.jarimanis.ui.photoView.ARG_PARAM1
import com.app.jarimanis.utils.Key
import kotlinx.android.synthetic.main.detail_thread_image_fragment.*
import kotlinx.android.synthetic.main.detail_thread_video_fragment.*
import java.net.URI

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

//    private lateinit var viewModel: ImageViewModel

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
        val imageListAdapter = ImageAdapter(this@ImageFragment)
        imageListAdapter.submitList(it.images)
        rcv_thumbnails.apply {

            adapter = imageListAdapter

        }
    }


    private fun cekPremission(item : Doc){
        if(item.user?.id != TokenUser.idUser){
            btn_newFoto.visibility = View.GONE
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}