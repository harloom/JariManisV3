package com.app.jarimanis.ui.dialogSheet

import android.app.Dialog
import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.thread.UserT
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.dialog_profile_fragment.view.*
import java.lang.Exception

class DialogProfile : DialogFragment(), View.OnClickListener {
    override fun onClick(v: View?) {

    }

    companion object {
        private const val EXTRA_ID = "id_user"


        fun newInstance(user : UserT): DialogProfile = DialogProfile().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_ID,user)
            }
        }
    }

    private lateinit var viewModel: DialogProfileViewModel
    private lateinit var cv_thumbail : CircleImageView
    private lateinit var tv_name : TextView
    private lateinit var iv_message : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_profile_fragment, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cv_thumbail = view.iv_thumbail
        iv_message = view.iv_message
        tv_name  = view.tv_user
        iv_message.setOnClickListener(this@DialogProfile)
        initUI()
    }

    private fun initUI() {
       val  user = arguments?.getParcelable<UserT>(EXTRA_ID) ?: throw IllegalStateException("No args provided")
        try {
            Glide.with(this@DialogProfile).load(user.thumbail).into(cv_thumbail)
            tv_name.text = user.nameUser
        }catch (e : Exception){

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun log(s: String) {
        Log.d(TAG, "Log : $s")
    }




}
