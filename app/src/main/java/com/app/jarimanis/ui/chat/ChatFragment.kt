package com.app.jarimanis.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.jarimanis.R

class ChatFragment : Fragment() {

    private lateinit var ViewModel: ChatViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        ViewModel =
                ViewModelProviders.of(this).get(ChatViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_chats, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        ViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}