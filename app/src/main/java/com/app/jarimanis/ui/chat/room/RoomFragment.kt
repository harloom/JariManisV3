package com.app.jarimanis.ui.chat.room

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
import com.app.jarimanis.data.datasource.models.chats.User
import com.app.jarimanis.data.datasource.models.message.ReciveMessage
import com.app.jarimanis.utils.Key
import com.app.jarimanis.utils.afterTextChanged
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.room_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class RoomFragment : Fragment(), Interaction {
    override fun onItemSelected(position: Int, item: ReciveMessage) {

    }

    companion object {
        fun newInstance() = RoomFragment()
    }

    private val sendOnclick: View.OnClickListener = View.OnClickListener {
        Toast.makeText(context!!,"Send Pesan..", Toast.LENGTH_LONG).show()
    }
    private var jobOnclick : Job?=null
    private val  viewModel: RoomViewModel by viewModel()
    private var jobChangeText : Job? = null
    private lateinit var messageListAdapter: MessageAdapter
    private lateinit var user : User

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
        getProfile(channel?.userList)
        initReclerView()
        initPorfile(channel)
        subcribeMessage(channel)


    }

    private fun getProfile(userList: List<User?>?) {
        userList?.map {
            if(it?.user?.id != TokenUser.idUser){
                user =  it!!
            }
        }
    }


    private fun initReclerView(){
        reyclerview_message_list.apply {
            messageListAdapter = MessageAdapter(this@RoomFragment,user)
            adapter = messageListAdapter
        }
    }
    private fun subcribeMessage(channel  : ChannelID?) {
            channel?.let {_channel->
                viewModel.getMessageRecive(_channel.id!!).observe(this@RoomFragment, Observer {
                    messageListAdapter.submitList(it.asReversed())
                    messageListAdapter.notifyDataSetChanged()
                })
            }
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
