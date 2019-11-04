package com.app.jarimanis.ui.thread.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.datasource.models.thread.Video
import com.app.jarimanis.utils.Key
import kotlinx.android.synthetic.main.detail_thread_video_fragment.*

class VideoFragment : Fragment(), VideoListAdapter.Interaction {
    override fun onItemSelected(position: Int, item: Video) {

    }


    private lateinit var vAdapter: VideoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_thread_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _id  = arguments?.getString(Key.THREADID)
        val doc = arguments?.getParcelable<Doc>(Key.THREAD)
        initRcv()
        doc?.let {
            initUi(it)
            cekPremission(it)
        }
    }

    private fun initRcv() {
        vAdapter = VideoListAdapter(this@VideoFragment)
        rcv_videos.apply {
            adapter = vAdapter
        }
    }

    private fun initUi(it: Doc) {
        println("video : $it")
        it.videos?.let {list->
            vAdapter.submitList(list)
        }

    }

    private fun cekPremission(item : Doc){
        if(item.user?.id != TokenUser.idUser){
            btn_newVideo.visibility = View.GONE
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        rcv_videos.adapter = null
    }
    override fun onDestroy() {
        super.onDestroy()

    }
}
