package com.app.jarimanis.ui.chat.room

import android.app.ActionBar
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.chats.ChannelID
import com.app.jarimanis.data.datasource.models.chats.Result
import com.app.jarimanis.utils.Key
import com.app.jarimanis.utils.afterTextChanged
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.custom_actionbar_chat.*
import kotlinx.android.synthetic.main.room_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RoomFragment : Fragment() {

    companion object {
        fun newInstance() = RoomFragment()
    }

    private val sendOnclick: View.OnClickListener = View.OnClickListener {
        Toast.makeText(context!!,"Send Pesan..", Toast.LENGTH_LONG).show()
    }
    private var jobOnclick : Job?=null
    private lateinit var viewModel: RoomViewModel
    private var jobChangeText : Job? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.room_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val result  = arguments?.getParcelable<Result>(Key.CHATRESULT)
        val channel = result?.channelID
        onRoomIn()
        initPorfile(channel)


    }

    private fun initPorfile(channel: ChannelID?) {
        channel?.userList?.map { user->
            if(user?.user?.id != TokenUser.idUser){
                println("Glide Enemy")
                try {
                    mView.findViewById<TextView>(R.id.title_text).text = user?.user?.nameUser
                    Glide.with(this@RoomFragment).load(user?.user?.thumbail).into(mView.findViewById(R.id.cv_userImage))
                }catch (e : Exception){
                    println("Error Glide : ${e}")
                }
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RoomViewModel::class.java)
        subcribeFrom()
        subcribeButtonSend()
        title_chat.setEndIconOnClickListener(sendOnclick)
    }

    private fun subcribeButtonSend() {
        viewModel.etMassage.observe(this@RoomFragment, Observer {
            title_chat.isEndIconVisible = it
        })
    }

    private fun subcribeFrom(){
        etMessage.afterTextChanged {
            jobChangeText?.cancel()
            jobChangeText = CoroutineScope(Main).launch {
                delay(300)
                viewModel.fromMassageChange(it)
            }
        }
    }
    private lateinit var mView : View
    private fun onRoomIn(){
         mView = LayoutInflater.from(context).inflate(R.layout.custom_actionbar_chat,null)
        (activity as AppCompatActivity).supportActionBar?.customView = mView
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    private fun onRoomExit(){
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        onRoomExit()
    }

}
