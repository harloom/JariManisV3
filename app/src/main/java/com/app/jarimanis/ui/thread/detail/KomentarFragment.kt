package com.app.jarimanis.ui.thread.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.utils.Key

class KomentarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_thread_komentar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _id  = arguments?.getString(Key.THREADID)
        val doc = arguments?.getParcelable<Doc>(Key.THREAD)

        doc?.let {
            initUi(it)
        }
    }

    private fun initUi(it: Doc) {

    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}
