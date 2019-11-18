package com.app.jarimanis.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.SentEditThreads
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.repository.thread.ThreadModelFactory
import com.app.jarimanis.data.repository.thread.users.ThreaduserModelFactory
import com.app.jarimanis.ui.thread.InteractionEditClick
import com.app.jarimanis.ui.thread.ThreadAdapter
import com.app.jarimanis.ui.thread.ThreadInfo
import com.app.jarimanis.ui.thread.detail.comentar.KomentarFragmentBottomSheet
import com.app.jarimanis.utils.Key
import com.app.jarimanis.utils.Key.THREAD
import com.app.jarimanis.utils.Key.THREADID
import com.app.jarimanis.utils.NetworkState
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.thread_me_list_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class ThreadMeFragment : Fragment(), ThreadUserAdapter.Interaction {
    override fun onBottomSheetUp(position: Int, item: Doc) {
        val bundleof = bundleOf(THREADID to item.id ,
            Key.THREAD to item)
        val kb = KomentarFragmentBottomSheet()
        kb.arguments = bundleof
        kb.show(childFragmentManager,"KomentarSheet")
    }

    override fun onLike(position: Int, item: Doc) {
        jobOnclick?.cancel()
        jobOnclick = CoroutineScope(Main).launch {
            delay(500)
            viewModel.likeThread(item,position)

        }
    }

    override fun onUnlike(position: Int, item: Doc) {
        jobOnclick?.cancel()
        jobOnclick = CoroutineScope(Main).launch {
            delay(500)
            viewModel.likeThread(item,position)

        }
    }

    override fun onItemLongSelected(position: Int, item: Doc) {
        goToMoreThread(item)
    }
    private fun goToMoreThread(item : Doc){
        val newFragment = ThreadInfo(item,onThreadCallback)

        // The device is using a large layout, so show the fragment as a dialog
        if (fragmentManager != null) {
            newFragment.show(childFragmentManager, "morePost")
        }


    }


    private val onThreadCallback: InteractionEditClick = object  : InteractionEditClick {
        override fun onDeleteListener(item: Doc) {
                viewModel.deleteThread(item)
        }

        override fun onEditListerner(id: String, title: String?, content: String?) {
            val editThreads : SentEditThreads = SentEditThreads(id = id ,content = content,title = title)
            viewModel.editThreads(editThreads)
        }

        override fun onLaporListener(item: Doc) {
            Toast.makeText(context , "onLaporListener " ,Toast.LENGTH_LONG).show()
        }

    }
    private var jobOnclick : Job?=null
    override fun onItemSelected(position: Int, item: Doc) {
        jobOnclick?.cancel()
        jobOnclick = CoroutineScope(Main).launch {
            delay(200)
            val bundleof = bundleOf(THREADID to item.id ,
                THREAD to item)
            findNavController().navigate(R.id.action_navigation_dashboard_to_threadDetailFragment2,bundleof)
        }
    }



    private lateinit var viewModel: ThreadMeListViewModel
    private lateinit var factory: ThreaduserModelFactory
    private lateinit var adapterT: ThreadUserAdapter

    private lateinit var mShimmer : ShimmerFrameLayout




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.thread_me_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mShimmer = view.findViewById(R.id.shimmer_animation)
        startAnimation()
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val uid = TokenUser.idUser!!
        initRecyleView(uid)

    }

    private fun initRecyleView(uid : String){

        adapterT = ThreadUserAdapter(this@ThreadMeFragment)
        factory = ThreaduserModelFactory(uid,get())
        viewModel = ViewModelProviders.of(this@ThreadMeFragment,factory).get(ThreadMeListViewModel::class.java)
        subcribeList()
        subcribeStatus()
    }

    private fun subcribeStatus() {
        viewModel.message.observe(this@ThreadMeFragment, Observer {
            if(!it.isNullOrBlank()){
                Toast.makeText(context,"Pesan :  $it",Toast.LENGTH_LONG).show()
            }
        })

        viewModel.onDelete.observe(this@ThreadMeFragment, Observer {
            if(it){
                Toast.makeText(context,"Threads Berhasil di Hapus",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun subcribeList() {

        rcv_thread.apply {
            adapter = adapterT
        }
        viewModel.records.observe(this@ThreadMeFragment, Observer {
            adapterT.submitList(it)
        })
        viewModel.initialLoad.observe(this@ThreadMeFragment, Observer {
            if (it == NetworkState.LOADING) {
                // Show loading

            } else {
                    stopAnimation()
                if (it.status == NetworkState.Status.SUCCESS_EMPTY) {
                    // Show empty state for initial load
                }
            }
        })
    }

    private fun stopAnimation() {

        CoroutineScope(Main).launch {
            mShimmer.stopShimmerAnimation()
            mShimmer.visibility = View.GONE
            delay(500)
        }

    }

    private fun startAnimation() {
        CoroutineScope(Main).launch {
            mShimmer.visibility = View.VISIBLE
            mShimmer.startShimmerAnimation()
            delay(500)
        }

    }
}
