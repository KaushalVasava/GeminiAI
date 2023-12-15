package com.lahsuak.apps.geminiai.di

import com.lahsuak.apps.geminiai.data.db.ChatDatabase
import com.lahsuak.apps.geminiai.ui.viewmodel.ChatViewModel
import com.lahsuak.apps.geminiai.ui.viewmodel.PhotoReasoningViewModel
import com.lahsuak.apps.geminiai.ui.viewmodel.SummarizeViewModel
import com.lahsuak.apps.geminiai.repo.ChatRepository
import com.lahsuak.apps.geminiai.repo.ChatRepositoryImpl
import com.lahsuak.apps.geminiai.repo.GeminiAIRepo
import com.lahsuak.apps.geminiai.repo.GeminiAIRepoImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::GeminiAIRepoImpl) { bind<GeminiAIRepo>() }
}

val viewModelModule = module {
    viewModelOf(::SummarizeViewModel)
    viewModelOf(::PhotoReasoningViewModel)
    viewModelOf(::ChatViewModel)
}
val databaseModule = module {
    single { ChatDatabase.getInstance(androidApplication()) }
    singleOf(::ChatRepositoryImpl){ bind<ChatRepository>()}
}