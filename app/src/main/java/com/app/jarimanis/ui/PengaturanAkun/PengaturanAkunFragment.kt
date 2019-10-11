package com.app.jarimanis.ui.PengaturanAkun

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.jarimanis.R


class PengaturanAkunFragment : Fragment() {

    companion object {
        fun newInstance() = PengaturanAkunFragment()
    }

    private lateinit var viewModel: PengaturanAkunViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pengaturan_akun_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PengaturanAkunViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
