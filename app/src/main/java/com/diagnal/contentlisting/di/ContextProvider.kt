package com.diagnal.contentlisting.di

import android.content.Context
import java.lang.ref.WeakReference

object ContextProvider {
    var context: WeakReference<Context>?=null
}