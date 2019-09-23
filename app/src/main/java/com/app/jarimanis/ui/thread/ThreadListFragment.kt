package com.app.jarimanis.ui.thread

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori
import com.app.jarimanis.utils.Key

class ThreadListFragment : Fragment() {

    companion object {
        fun newInstance() = ThreadListFragment()
    }

    private lateinit var viewModel: ThreadListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.thread_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val result  = arguments?.getParcelable<ResultKategori>(Key.argCategory)
        println(result)
        activity!!.title = result?.category
        viewModel = ViewModelProviders.of(this).get(ThreadListViewModel::class.java)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }





}
