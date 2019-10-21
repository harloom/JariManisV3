package com.app.jarimanis.ui.thread.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.data.repository.commentar.DiskusiModelFactory
import com.app.jarimanis.data.repository.thread.ThreadModelFactory
import com.app.jarimanis.ui.thread.comentar.ComentarAdapter
import com.app.jarimanis.ui.thread.comentar.KomentarViewModel
import com.app.jarimanis.utils.Key
import kotlinx.android.synthetic.main.detail_thread_komentar_fragment.*
import org.koin.android.ext.android.get

class KomentarFragment : Fragment(), ComentarAdapter.Interaction {
    override fun onItemSelected(
        position: Int,
        item: com.app.jarimanis.data.datasource.models.diskusi.paging.Doc
    ) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_thread_komentar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val doc = arguments?.getParcelable<Doc>(Key.THREAD)

        doc?.let {
            initUi(it)
        }
    }

    private fun initUi(it: Doc) {

    }


    private lateinit var viewModel: KomentarViewModel
    private lateinit var factory: DiskusiModelFactory
    private lateinit var mAdapter: ComentarAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val _id  = arguments?.getString(Key.THREADID)
        _id?.let {
            mAdapter = ComentarAdapter(this@KomentarFragment)
            factory = DiskusiModelFactory(_id,get())
            viewModel = ViewModelProviders.of(this@KomentarFragment,factory).get(KomentarViewModel::class.java)
            subcribeList()
        }

    }

    private fun subcribeList() {
        rcv_commentar.apply {
            adapter = mAdapter
        }

        viewModel.records.observe(this@KomentarFragment, Observer {
            mAdapter.submitList(it)
        })
    }

}
