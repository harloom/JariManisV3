package com.app.jarimanis.ui.thread.detail.comentar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.diskusi.SaveCommentar
import com.app.jarimanis.data.datasource.models.thread.Doc

import com.app.jarimanis.data.repository.commentar.DiskusiModelFactory
import com.app.jarimanis.utils.Key
import com.app.jarimanis.utils.NetworkState
import com.app.jarimanis.utils.afterTextChanged
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_thread_komentar_fragment.*
import kotlinx.android.synthetic.main.detail_thread_komentar_fragment.*
import kotlinx.android.synthetic.main.detail_thread_komentar_fragment.balasanLayout
import kotlinx.android.synthetic.main.detail_thread_komentar_fragment.etKomentar
import kotlinx.android.synthetic.main.detail_thread_komentar_fragment.rcv_commentar
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.koin.android.ext.android.get

class KomentarFragmentBottomSheet : BottomSheetDialogFragment(), ComentarAdapter.Interaction, View.OnClickListener {

    override fun onBtnLikeClick(
        position: Int,
        item: com.app.jarimanis.data.datasource.models.diskusi.paging.Doc
    ) {
        jobOnclick?.cancel()
        jobOnclick = CoroutineScope(Main).launch {
            delay(500)
            viewModel.likeKomentar(item,position)

        }
    }


    private var doc: Doc? = null
    private var jobOnclick : Job?=null
    private var jobChangeText : Job? = null

    override fun onClick(v: View?) {
        jobOnclick?.cancel()
        doc?.let {d->
            jobOnclick = CoroutineScope(IO).launch {
                delay(400)
                viewModel.postCommentar(d.id!!, SaveCommentar(content = etKomentar.text.toString()))
                withContext(Main){
                    etKomentar.setText("")
                }

            }
        }



    }

    override fun onItemLongSelected(
        position: Int,
        item: com.app.jarimanis.data.datasource.models.diskusi.paging.Doc
    ) {
        if(item.user?.id == TokenUser.idUser){
            goToMoreThread(item , position)
        }

    }
    private val onMoredCallback: InteractionEditClick = object  : InteractionEditClick{
        override fun onEditListerner(id: String, content: String?) {

        }

        override fun onDeleteListener(
            item: com.app.jarimanis.data.datasource.models.diskusi.paging.Doc,
            position: Int
        ) {
            viewModel.deleteCommentar(item ,position)
        }



        override fun onLaporListener(item: com.app.jarimanis.data.datasource.models.diskusi.paging.Doc) {

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
        return inflater.inflate(R.layout.bottom_thread_komentar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doc = arguments?.getParcelable<Doc>(Key.THREAD)
        startAnimation()
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
            mAdapter = ComentarAdapter(this@KomentarFragmentBottomSheet)
            factory = DiskusiModelFactory(_id,get())
            viewModel = ViewModelProviders.of(this@KomentarFragmentBottomSheet,factory).get(KomentarViewModel::class.java)
            subcribeList()
            println("bottomShhet : $_id")
        }
        balasanLayout.setEndIconOnClickListener(this@KomentarFragmentBottomSheet)
        subcribeFrom()
        subcribeButtonSend()
        subcribeRespon()
        subcribeStatus()
    }

    private fun subcribeRespon() {
        viewModel.respon.observe(this@KomentarFragmentBottomSheet, Observer {
            if(it !=null){
                if(it.isSuccessful){
                    Toast.makeText(context,"Komentar Berhasil" , Toast.LENGTH_SHORT).show()
                }else if(it.code() == 400){
                    println(it.body())
                    viewModel.onRefress()
                    Toast.makeText(context,"Terjadi Kesalahan, Coba Kembali" , Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun subcribeButtonSend() {
        balasanLayout.isEndIconVisible = false
        viewModel.etKomentar.observe(this@KomentarFragmentBottomSheet, Observer {
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

        viewModel.records.observe(this@KomentarFragmentBottomSheet, Observer {
            mAdapter.submitList(it)
        })
    }

    private fun subcribeStatus() {
        viewModel.initialLoad.observe(this@KomentarFragmentBottomSheet, Observer {
            if (it == NetworkState.LOADING) {
                // Show loading

            } else {
                stopAnimation()
                if (it.status == NetworkState.Status.SUCCESS_EMPTY) {
                    // Show empty state for initial load
                }
            }
        })
        viewModel.onDelete.observe(this@KomentarFragmentBottomSheet, Observer {
            if(it.onDelete!!){
                mAdapter.notifyItemRemoved(it.adapterPosition!!)
                viewModel.onRefress()
                Toast.makeText(context,"Commentar Berhasil di Hapus",Toast.LENGTH_LONG).show()
            }
            if(!it.onDelete){
                Toast.makeText(context,"Something is Error please Try Again!",Toast.LENGTH_LONG).show()
                viewModel.onRefress()
            }
        })
    }

    private fun goToMoreThread(
        item: com.app.jarimanis.data.datasource.models.diskusi.paging.Doc,
        position: Int
    ){
        val newFragment = ComentarInfo(item,position,onMoredCallback)

        // The device is using a large layout, so show the fragment as a dialog
        if (fragmentManager != null) {
            newFragment.show(childFragmentManager, "morePost")
        }


    }


    private fun stopAnimation() {
        val s = shimmer_animation
        CoroutineScope(Main).launch {
            s.stopShimmerAnimation()
            s.visibility = View.GONE
            delay(500)
        }

    }

    private fun startAnimation() {
        CoroutineScope(Main).launch {
            shimmer_animation.visibility = View.VISIBLE
            shimmer_animation.startShimmerAnimation()
            delay(500)
        }

    }
}
