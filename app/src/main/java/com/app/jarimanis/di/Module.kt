package com.app.jarimanis.di

import org.koin.dsl.module

val appModule  = module{
    // single instance of HelloRepository
    // single<HelloRepository> { HelloRepositoryImpl() }

//    factory { MenuRepositroyImp(get()) }
//    factory { UserRepositoryImp(get()) }
//    single { productRepositroyImp(get()) }
//
//    // MyViewModel ViewModel
//    viewModel { RegisterViewModel(get()) }
//    viewModel { HomeViewModel(get()) }
}