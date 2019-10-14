package com.app.jarimanis.ui.PengaturanAkun

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.app.jarimanis.R
import com.app.jarimanis.ui.dashboard.DashboardViewModel
import com.app.jarimanis.utils.TimeFormater
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pengaturan_akun_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel


class PengaturanAkunFragment : Fragment() {

    companion object {
        fun newInstance() = PengaturanAkunFragment()
    }


    private val  vm: DashboardViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pengaturan_akun_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title ="Pengaturan Akun"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm.myProfile.observe(this@PengaturanAkunFragment, Observer {

            tv_displayName.text = it.nameUser
            tv_displayEmail.text = it.emailUser
            tv_displayNumber.text = it.numberPhone
            it.birthDayUser?.let {s->
                tv_displayBirthDay.text = TimeFormater.StringToDate(s)
            }
            initImage(it.thumbail)
        })

    }

    private fun initImage(s : String?){
        try {
            s.let {
                Glide.with(this@PengaturanAkunFragment).load(s).into(ivProfPict)
            }

        }catch (e : Exception){

        }
    }

}
