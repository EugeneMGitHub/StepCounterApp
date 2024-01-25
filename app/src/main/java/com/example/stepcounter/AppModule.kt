package com.example.stepcounter

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMainViewModel(): MainViewModel {
        return MainViewModel()
    }

    // Остальные предоставления зависимостей...
}