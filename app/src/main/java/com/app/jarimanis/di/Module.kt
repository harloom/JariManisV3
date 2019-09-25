package com.app.jarimanis.di

import com.app.jarimanis.data.repository.firebase.UserRepositoryImp
import com.app.jarimanis.data.repository.profile.ProfileRepository
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.data.repository.thread.ThreadRepositoryImp
import com.app.jarimanis.ui.auth.RegisterViewModel
import com.app.jarimanis.ui.dashboard.DashboardViewModel
import com.app.jarimanis.ui.home.HomeViewModel
import com.app.jarimanis.ui.home.KategoriRepository

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule  = module{
    // single instance of HelloRepository
    // single<HelloRepository> { HelloRepositoryImpl() }

//    factory { MenuRepositroyImp(get()) }
    single { UserRepositoryImp(get()) }
    single { ProfileRepository(get()) }

    single <KategoriRepository>{ KategoriRepository(get()) }
    single<ThreadRepository>{ThreadRepositoryImp(get())}

    viewModel { HomeViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}