package com.app.jarimanis.ui.thread

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.repository.thread.ThreadModelFactory
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

class ThreadListFragment : Fragment(), ThreadAdapter.Interaction {
    private  var jobDetail : Job?= null
    private var jobOnclick : Job?=null
    override fun onItemSelected(position: Int, item: Doc) {
        jobDetail?.cancel()
        jobDetail = CoroutineScope(Main).launch {
            delay(500)
            val bundleof = bundleOf(THREADID to item.id ,
                THREAD to item)
            findNavController().navigate(R.id.action_threadListFragment_to_threadDetailFragment,bundleof)
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
            CoroutineScope(Main).launch {
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





}
