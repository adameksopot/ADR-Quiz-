package org.example.project.di

import org.example.project.screens.asker.QuestionMapper
import org.example.project.data.QuestionRepository
import org.example.project.screens.asker.AskerViewModel
import org.example.project.screens.welcome.WelcomeViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val appModule = module {
    single<QuestionRepository> { QuestionRepository() }
    single { WelcomeViewModel(get()) }
    single { AskerViewModel(get(), get()) }
    singleOf(::QuestionMapper)
}


fun initKoin() {
    startKoin {
        modules(appModule)
    }
}