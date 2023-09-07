package com.diagnal.contentlisting.di

import com.diagnal.contentlisting.repo.IPageRepository
import com.diagnal.contentlisting.repo.PageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContentModule {

    @Singleton
    @Provides
    fun jsonLoader(): IPageRepository = PageRepositoryImpl()
}