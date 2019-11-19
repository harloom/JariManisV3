package com.app.jarimanis.ui.thread.detail

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.ui.thread.InteractionEditClick
import com.app.jarimanis.ui.thread.ThreadInfo
import com.app.jarimanis.utils.Key.THREAD
import com.app.jarimanis.utils.Key.THREADID
import com.app.jarimanis.utils.debounce.onClickDebounced
import com.bumptech.glide.Glide
import com.snov.timeagolibrary.PrettyTimeAgo
import kotlinx.android.synthetic.main.thread_detail_fragment.*

class ThreadDetailFragment : Fragment(), InteractionEditClick {
    override fun onDeleteListener(item: Doc) {

    }

    override fun onEditListerner(id: String, title: String?, content: String?) {

    }

    override fun onLaporListener(item: Doc) {

    }

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
        initClick(doc)

    }

    private fun initClick(doc: Doc?) {
        iv_more.onClickDebounced {
            if(doc!!.user?.id == TokenUser.idUser){
                goToMoreThread(doc)
            }else {
                showBottomSheetListCommentar(BottomSheet(LayoutMode.WRAP_CONTENT), doc)
            }
        }
    }

    private fun goToMoreThread(item: Doc) {
        val newFragment = ThreadInfo(item,this@ThreadDetailFragment)

        // The device is using a large layout, so show the fragment as a dialog
        if (fragmentManager != null) {
            newFragment.show(childFragmentManager, "morePost")
        }

    }


    private  fun initUI(doc: Doc?) {
        tv_title.text = doc?.title?.capitalize()
        tv_content.text = doc?.content?.capitalize()

        try {
            tv_user.text = doc?.user?.nameUser?.capitalize()
            Glide.with(context!!).load(doc?.user?.thumbail).into(cv_thumbail)
            tv_time.text = PrettyTimeAgo.getTimeAgo(doc?.updateAt!!)
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


    private fun showBottomSheetListCommentar(dialogBehavior: DialogBehavior = ModalDialog, item: Doc){
        val dialog = MaterialDialog(context!!, dialogBehavior).show {
            cornerRadius(16f)
            customView(R.layout.bottom_sheet_count, scrollable = true, horizontalPadding = true)
            lifecycleOwner(this@ThreadDetailFragment)

        }
        val view = dialog.getCustomView()
        val mDiskusi  = view.findViewById<TextView>(R.id.tv_count_diskusi)
        val mLike = view.findViewById<TextView>(R.id.tv_count_like)

        mDiskusi.setText(item.diskusiCount.toString())
        if(item.likes!!.isNotEmpty()){
            mLike.setText(item.likes.size.toString())
        }



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
