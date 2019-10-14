package com.app.jarimanis.di

import com.app.jarimanis.ui.auth.LoginViewModel
import com.app.jarimanis.ui.auth.RegisterViewModel
import com.app.jarimanis.ui.chat.ChatViewModel
import com.app.jarimanis.ui.chat.room.RoomViewModel
import com.app.jarimanis.ui.dashboard.DashboardViewModel
import com.app.jarimanis.ui.home.HomeViewModel
import com.app.jarimanis.ui.thread.post.CreateThreadViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vnModule  = module{


    viewModel { HomeViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { CreateThreadViewModel(get(),get()) }
    viewModel { ChatViewModel(get()) }
    viewModel { RoomViewModel(get()) }
}