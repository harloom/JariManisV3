package com.app.jarimanis.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.pemberitahuan.Doc
import com.app.jarimanis.data.repository.pemberitahuan.PemberitahuanModelFactory
import com.app.jarimanis.utils.Key.THREAD
import com.app.jarimanis.utils.Key.THREADID
import com.app.jarimanis.utils.NetworkState
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class NotificationsFragment : Fragment(), PemberitahuanListAdapter.Interaction {
    private var jobOnclick : Job?=null
    override fun onItemSelected(position: Int, item: Doc) {
//        jobOnclick?.cancel()
//        jobOnclick = CoroutineScope(Main).launch {
//            delay(200)
//            val bundleof = bundleOf(THREADID to item.threadId!!.id)
//            findNavController().navigate(R.id.action_navigation_notifications_to_threadDetailFragment2,bundleof)
//        }
    }

    private lateinit var notificationsViewModel: PemberitahuanViewModel
    private lateinit var factory: PemberitahuanModelFactory
    private lateinit var mAdapter: PemberitahuanListAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        factory = PemberitahuanModelFactory(get())
        notificationsViewModel =
                ViewModelProviders.of(this@NotificationsFragment,factory).get(PemberitahuanViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title ="Pemberitahuan"


        subcribeList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    private fun subcribeList() {
        //animation


        notificationsViewModel.initialLoad.observe(this@NotificationsFragment, Observer {
            if (it == NetworkState.LOADING) {
                // Show loading

            } else {
                // Hide loading

                if (it.status == NetworkState.Status.SUCCESS_EMPTY) {
                    // Show empty state for initial load
                }
            }
        })

        mAdapter = PemberitahuanListAdapter(this@NotificationsFragment)
        rcv_pemberitahuan.apply {
            adapter = mAdapter
        }

        notificationsViewModel.records.observe(this@NotificationsFragment, Observer {
            mAdapter.submitList(it)
        })
    }

}