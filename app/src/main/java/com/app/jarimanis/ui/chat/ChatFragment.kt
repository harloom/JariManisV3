package com.app.jarimanis.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.MenuData
import com.app.jarimanis.data.datasource.models.chats.Result
import com.app.jarimanis.utils.Key.CHATRESULT
import com.app.jarimanis.utils.Key.THREAD
import com.app.jarimanis.utils.Key.THREADID
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class ChatFragment : Fragment(), ChatChannelListAdapter.Interaction {
    override fun onItemSelected(position: Int, item: Result) {
        jobOnclick?.cancel()
        jobOnclick = CoroutineScope(Main).launch {
            delay(200)
            val bundleof = bundleOf(CHATRESULT to item)
            findNavController().navigate(R.id.action_navigation_chat_to_chat_navigation,bundleof)
        }
    }
    private var jobOnclick : Job?=null
    private val vm : ChatViewModel by viewModel()
    private lateinit var channelsListAdapter: ChatChannelListAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_chats, container, false)
        return root
    }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    activity?.title ="Percakapan"
      initReclerView()
      subcribeMyChannels()
      setOptionNotif()

  }

    private fun setOptionNotif() {
        if(MenuData.chatExits){
            MenuData.chatExits = false
        }
    }

    private fun initReclerView(){
        rcv_channels.apply {
            channelsListAdapter = ChatChannelListAdapter(this@ChatFragment)
            adapter = channelsListAdapter
        }
    }

    private fun subcribeMyChannels() {

        vm.myChatChannels?.observe(this@ChatFragment, Observer {
            it?.let { ch->
                channelsListAdapter.submitList(ch)
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}