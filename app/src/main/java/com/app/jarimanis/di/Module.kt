package com.app.jarimanis.di

import com.app.jarimanis.ui.home.HomeViewModel
import com.app.jarimanis.ui.home.KategoriRepository

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule  = module{
    // single instance of HelloRepository
    // single<HelloRepository> { HelloRepositoryImpl() }

//    factory { MenuRepositroyImp(get()) }
//    factory { UserRepositoryImp(get()) }
//    single { productRepositroyImp(get()) }

    single <KategoriRepository>{ KategoriRepository(get()) }

    viewModel { HomeViewModel(get()) }
}