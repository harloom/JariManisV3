package com.app.jarimanis.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori
import com.app.jarimanis.utils.Key
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), KategoriListAdapter.Interaction {
    override fun onItemSelected(position: Int, item: ResultKategori) {
        Toast.makeText(context, item.category ,Toast.LENGTH_LONG).show()
        val bundleof = bundleOf(Key.argCategory to item)
        findNavController().navigate(R.id.action_navigation_home_to_thread_navigation,bundleof)
    }


    private val subcribeKategory = Observer<List<ResultKategori?>>{

        categoryAdapter.submitList(it)
        stopAnimation()
        swp_records.isRefreshing = false

    }
    private lateinit var categoryAdapter : KategoriListAdapter
    private val  homeViewModel: HomeViewModel by viewModel()
    private var jobSwipe : Job? =null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title ="Home"
        swipe(view)
    }

    private fun swipe(view: View) {
        view.swp_records.setOnRefreshListener {
            jobSwipe?.cancel()
            jobSwipe  = CoroutineScope(Main).launch {
                startAnimation()
                CoroutineScope(Main).launch {
                    delay(3000)

                    homeViewModel.refress()
                }

            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initReclerView()
        homeViewModel.kategori.observe(this@HomeFragment,subcribeKategory)

    }

    private fun initReclerView(){
        rcv_category.apply {
            categoryAdapter = KategoriListAdapter(this@HomeFragment)
            adapter = categoryAdapter
        }
    }

    private fun stopAnimation() {
        try {
            CoroutineScope(Main).launch {
                shimmer_category.stopShimmerAnimation()
                shimmer_category.visibility = View.GONE
                rcv_category?.visibility = View.VISIBLE
                delay(2000)

            }
        }catch (e : Exception){
            println("Error: $e Shimmer Stop")
        }


    }

    private fun startAnimation() {
        CoroutineScope(Main).launch {
            try {
                shimmer_category.visibility = View.VISIBLE
                rcv_category?.visibility = View.GONE
                shimmer_category.startShimmerAnimation()
                delay(2000)

            }catch (e :Exception){

            }


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("On onDestroyView")

    }

    override fun onResume() {
        super.onResume()
        println("On onResume")
    }

    override fun onPause() {
        super.onPause()
        println("On onPause")
    }

    override fun onStop() {
        super.onStop()
        println("On Stop")
    }


}