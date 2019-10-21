package com.app.jarimanis.di

import com.app.jarimanis.data.repository.chat.ChatRepository
import com.app.jarimanis.data.repository.chat.ChatRepositoryImp
import com.app.jarimanis.data.repository.commentar.ComentarRepositoryImp
import com.app.jarimanis.data.repository.commentar.DiskusiRepository
import com.app.jarimanis.data.repository.firebase.TokenRepository
import com.app.jarimanis.data.repository.firebase.UserRepositoryImp
import com.app.jarimanis.data.repository.profile.ProfileRepositoryImp
import com.app.jarimanis.data.repository.roomChat.RoomChatRepository
import com.app.jarimanis.data.repository.roomChat.RoomChatRepositoryImp
import com.app.jarimanis.data.repository.thread.ThreadRepository
import com.app.jarimanis.data.repository.thread.ThreadRepositoryImp
import com.app.jarimanis.ui.home.KategoriRepository

import org.koin.dsl.module

val appModule  = module{
    // single instance of HelloRepository
    // single<HelloRepository> { HelloRepositoryImpl() }

//    factory { MenuRepositroyImp(get()) }
    single { UserRepositoryImp(get()) }
    single { ProfileRepositoryImp(get()) }
    single { TokenRepository(get()) }
    single <ChatRepository> { ChatRepositoryImp(get()) }
    single <RoomChatRepository>{ RoomChatRepositoryImp(get()) }
    single <KategoriRepository>{ KategoriRepository(get()) }
    single<ThreadRepository>{ThreadRepositoryImp(get())}
    single<DiskusiRepository> {ComentarRepositoryImp(get())  }


}