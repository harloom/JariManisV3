package com.app.jarimanis.ui.auth

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.jarimanis.R
import kotlinx.android.synthetic.main.forgot_fragment.*


class ForgotFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back_arrow->{
                findNavController().navigateUp()
            }
        }
    }

    companion object {
        fun newInstance() = ForgotFragment()
    }

    private lateinit var viewModel: ForgotViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forgot_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_back_arrow.setOnClickListener(this@ForgotFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForgotViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
