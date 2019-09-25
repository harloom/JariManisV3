package com.app.jarimanis.ui.thread.detail

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.utils.Key.THREAD
import com.app.jarimanis.utils.Key.THREADID
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.thread_detail_fragment.*

class ThreadDetailFragment : Fragment() {

    companion object {
        fun newInstance() = ThreadDetailFragment()
    }

    private lateinit var viewModel: ThreadDetailViewModel
    private lateinit var viewPager: ViewPager
    private lateinit var adpaterPager: PagerDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.thread_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title =""
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ThreadDetailViewModel::class.java)
        val _id  = arguments?.getString(THREADID)
        val doc = arguments?.getParcelable<Doc>(THREAD)
        adpaterPager = PagerDetailAdapter(_id , doc,childFragmentManager)
        initPager()
        initUI(doc)

    }


    private  fun initUI(doc: Doc?) {
        tv_title.text = doc?.title?.capitalize()
        tv_content.text = doc?.content?.capitalize()
        try {
            tv_user.text = doc?.user?.nameUser?.capitalize()
            Glide.with(context!!).load(doc?.user?.thumbail).into(cv_thumbail)
        }catch (e : Exception){

        }
    }
    private fun initPager(){
        viewPager = pager_detail
        val offset = 4
        viewPager.offscreenPageLimit  =offset
        viewPager.adapter = adpaterPager
        viewPager.addOnPageChangeListener(pageOnScrool)
        val tabLayout = tab_layout
        tabLayout.setupWithViewPager(viewPager)
    }

    private val pageOnScrool = object  : ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {

        }

    }

}
