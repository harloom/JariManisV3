package com.app.jarimanis.ui.dialog

import android.app.Dialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.models.chats.User
import com.app.jarimanis.data.datasource.models.chats.UserChat
import com.app.jarimanis.data.datasource.models.thread.UserT
import com.app.jarimanis.utils.Key.USERCHAT
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.dialog_profile_fragment.view.*
import java.lang.Exception

class DialogProfile : DialogFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        val user_ : User = User(UserChat("",user.id,user.nameUser,"",user.thumbail),"","")
        val bundleof = bundleOf(USERCHAT to user_)
        findNavController().navigate(R.id.action_threadListFragment_to_chat_navigation,bundleof)
    }

    companion object {
        private const val EXTRA_ID = "id_user"


        fun newInstance(user : UserT): DialogProfile = DialogProfile().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_ID,user)
            }
        }
    }

    private  lateinit var user: UserT
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
            dialog.window!!.setBackgroundDrawableResource(R.drawable.corner_bottom)
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cv_thumbail = view.iv_thumbail
        iv_message = view.iv_message
        tv_name  = view.tv_user
        iv_message.setOnClickListener(this@DialogProfile)
        val mActionDismiss = view.action_dismiss
        mActionDismiss.setOnClickListener {
            this@DialogProfile.dismiss()
        }
        initUI()
    }

    private fun initUI() {
       user = arguments?.getParcelable<UserT>(EXTRA_ID) ?: throw IllegalStateException("No args provided")
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
