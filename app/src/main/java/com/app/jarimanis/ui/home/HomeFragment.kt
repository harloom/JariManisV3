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
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), KategoriListAdapter.Interaction {
    override fun onItemSelected(position: Int, item: ResultKategori) {
        Toast.makeText(context, item.category ,Toast.LENGTH_LONG).show()
        val bundleof = bundleOf(Key.argCategory to item)
        findNavController().navigate(R.id.action_navigation_home_to_thread_navigation,bundleof)
    }


    private val subcribeKategory = Observer<List<ResultKategori?>>{
    categoryAdapter.submitList(it)
    }
    private lateinit var categoryAdapter : KategoriListAdapter
    private val  homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        println("On onCreateView")
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("On onViewCreated")
        activity?.title ="Home"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("On onActivityCreated")
        initReclerView()
        homeViewModel.kategori.observe(this@HomeFragment,subcribeKategory)
    }

    private fun initReclerView(){
        rcv_category.apply {
            categoryAdapter = KategoriListAdapter(this@HomeFragment)
            adapter = categoryAdapter
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