package com.diagnal.contentlisting

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ContentApplication: Application() {
    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }
}