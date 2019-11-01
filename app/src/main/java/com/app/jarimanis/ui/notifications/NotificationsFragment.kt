package com.app.jarimanis.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.pemberitahuan.Doc
import com.app.jarimanis.data.repository.pemberitahuan.PemberitahuanModelFactory
import kotlinx.android.synthetic.main.fragment_notifications.*
import org.koin.android.ext.android.get

class NotificationsFragment : Fragment(), PemberitahuanListAdapter.Interaction {
    override fun onItemSelected(position: Int, item: Doc) {

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
        mAdapter = PemberitahuanListAdapter(this@NotificationsFragment)
        rcv_pemberitahuan.apply {
            adapter = mAdapter
        }

        notificationsViewModel.records.observe(this@NotificationsFragment, Observer {
            mAdapter.submitList(it)
        })
    }
}