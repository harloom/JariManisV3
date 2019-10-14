package com.app.jarimanis.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.profile.Result
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {


    private val  vm: DashboardViewModel by viewModel()



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title ="Akun Saya"
        vm.myProfile.observe(this@DashboardFragment,subcribeProfile)
        buttonProfileOnclick()

    }

    private fun buttonProfileOnclick() {
        btnPengaturanUser.setOnClickListener {
          findNavController().navigate(R.id.action_navigation_dashboard_to_pengaturanAkunFragment)
        }
    }

    private val subcribeProfile = Observer<Result> {
        initProfile(it)
    }

    private  fun initProfile(items : Result){
        try {
            tv_user.text = items.nameUser
            Glide.with(this@DashboardFragment).load(items.thumbail).into(iv_thumbail)


        }catch (e: Exception){
            Toast.makeText(context!!,"Something Erorr, please refress!" , Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}