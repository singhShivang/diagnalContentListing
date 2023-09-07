package com.diagnal.contentlisting.di

import com.diagnal.contentlisting.views.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class, ContentModule::class]
)

interface ContentComponent {
    fun inject(activity: MainActivity)


    @Component.Builder
    interface Builder {
        fun build(): ContentComponent
    }
}