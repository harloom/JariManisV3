package com.app.jarimanis.ui.thread

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.SentEditThreads
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.repository.thread.ThreadModelFactory
import com.app.jarimanis.ui.dialog.DialogProfile
import com.app.jarimanis.ui.thread.detail.comentar.KomentarFragmentBottomSheet
import com.app.jarimanis.utils.Key
import com.app.jarimanis.utils.Key.THREAD
import com.app.jarimanis.utils.Key.THREADID
import kotlinx.android.synthetic.main.thread_list_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class ThreadListFragment : Fragment(), ThreadAdapter.Interaction  {
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
        if(item.user?.id == TokenUser.idUser){
            goToMoreThread(item)
        }


    }


    override fun onProfileSelected(position: Int, item: Doc) {
        jobOnclick?.cancel()
        jobOnclick = CoroutineScope(Main).launch {
            delay(500)
            if(item.user?.id != TokenUser.idUser){
                val d = DialogProfile.newInstance(item.user!!)
                d.show(childFragmentManager,"Profile")
            }


        }
    }


    private var jobOnclick : Job?=null
    override fun onItemSelected(position: Int, item: Doc) {
        jobOnclick?.cancel()
        jobOnclick = CoroutineScope(Main).launch {
            delay(400)
            val bundleof = bundleOf(THREADID to item.id ,
                THREAD to item)
            try {
                findNavController().navigate(R.id.action_threadListFragment_to_threadDetailFragment,bundleof)
            }catch (e  : Exception){

            }

        }

    }

    companion object {
        fun newInstance() = ThreadListFragment()
    }

    private lateinit var viewModel: ThreadListViewModel
    private lateinit var factory: ThreadModelFactory
    private lateinit var adapterT: ThreadAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.thread_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val result  = arguments?.getParcelable<ResultKategori>(Key.argCategory)
        activity!!.title = result?.category
        btn_create.setOnClickListener {
            jobOnclick?.cancel()
            jobOnclick = CoroutineScope(Main).launch {
                delay(300)
                val bundled = bundleOf(Key.argCategory to result)
                findNavController().navigate(R.id.action_threadListFragment_to_createThreadFragment,bundled)
            }
        }


        result?.id?.let {_id->
            adapterT = ThreadAdapter(this@ThreadListFragment)
            factory = ThreadModelFactory(_id,get())
            viewModel = ViewModelProviders.of(this@ThreadListFragment,factory).get(ThreadListViewModel::class.java)
            subcribeList()
            subcribeStatus()
        }


        onRefress()
    }


    private var jobRefress : Job? =null
    private fun onRefress() {
        refress.setOnRefreshListener {
            jobRefress?.cancel()
            jobRefress = CoroutineScope(Main).launch{
                    delay(500)
                    viewModel.onRefress()
                    refress.isRefreshing = false
            }
        }
    }


    private fun subcribeList() {
        rcv_thread.apply {
            adapter = adapterT
        }
        viewModel.records.observe(this@ThreadListFragment, Observer {
            adapterT.submitList(it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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


    private fun subcribeStatus() {
        viewModel.message.observe(this@ThreadListFragment, Observer {
            if(!it.isNullOrBlank()){
                Toast.makeText(context,"Pesan :  $it",Toast.LENGTH_LONG).show()
            }
        })

        viewModel.onDelete.observe(this@ThreadListFragment, Observer {
            if(it){
                Toast.makeText(context,"Threads Berhasil di Hapus",Toast.LENGTH_LONG).show()
            }
        })

        viewModel.onLike.observe(this@ThreadListFragment, Observer {
                if(it.item !=null && it.position !=null){
//                    adapterT.notifyItemChanged(it.position)
                }
        })
    }

}
