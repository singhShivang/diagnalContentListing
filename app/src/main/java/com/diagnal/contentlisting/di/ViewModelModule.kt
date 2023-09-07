package com.diagnal.contentlisting.di



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.diagnal.contentlisting.viewModels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityComponent::class)
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel):ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ContentViewModelFactory): ViewModelProvider.Factory
}