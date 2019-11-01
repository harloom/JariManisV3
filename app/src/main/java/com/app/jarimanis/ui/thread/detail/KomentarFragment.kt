package com.app.jarimanis.ui.thread.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.diskusi.SaveCommentar
import com.app.jarimanis.data.datasource.models.thread.Doc

import com.app.jarimanis.data.repository.commentar.DiskusiModelFactory
import com.app.jarimanis.data.repository.thread.ThreadModelFactory
import com.app.jarimanis.ui.thread.comentar.ComentarAdapter
import com.app.jarimanis.ui.thread.comentar.KomentarViewModel
import com.app.jarimanis.utils.Key
import com.app.jarimanis.utils.afterTextChanged
import kotlinx.android.synthetic.main.detail_thread_komentar_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.koin.android.ext.android.get

class KomentarFragment : Fragment(), ComentarAdapter.Interaction, View.OnClickListener {


    private var doc: Doc? = null
    private var jobOnclick : Job?=null
    private var jobChangeText : Job? = null

    override fun onClick(v: View?) {
        jobOnclick?.cancel()
        doc?.let {d->
            jobOnclick = CoroutineScope(IO).launch {
                delay(200)
                viewModel.postCommentar(d.id!!, SaveCommentar(content = etKomentar.text.toString()))
                withContext(Main){
                    etKomentar.setText("")
                }

            }
        }

    }

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
         doc = arguments?.getParcelable<Doc>(Key.THREAD)

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
        balasanLayout.setEndIconOnClickListener(this@KomentarFragment)
        subcribeFrom()
        subcribeButtonSend()
        subcribeRespon()
    }

    private fun subcribeRespon() {
        viewModel.respon.observe(this@KomentarFragment, Observer {
            if(it !=null){
                if(it.isSuccessful){
                    Toast.makeText(context,"Komentar Berhasil" , Toast.LENGTH_SHORT).show()
                }else if(it.code() == 400){
                    Toast.makeText(context,"Terjadi Kesalahan, Coba Kembali" , Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun subcribeButtonSend() {
        balasanLayout.isEndIconVisible = false
        viewModel.etKomentar.observe(this@KomentarFragment, Observer {
            balasanLayout.isEndIconVisible = it
        })
    }
    private fun subcribeFrom(){
        etKomentar.afterTextChanged {
            jobChangeText?.cancel()
            jobChangeText = CoroutineScope(Main).launch {
                delay(300)
                viewModel.fromMassageChange(it)
            }
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
