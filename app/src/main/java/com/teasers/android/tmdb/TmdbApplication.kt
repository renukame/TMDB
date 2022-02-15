package com.teasers.android.tmdb

import android.app.Application
import com.teasers.android.tmdb.di.component.AppComponent
import com.teasers.android.tmdb.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class TmdbApplication : Application(), HasAndroidInjector {
    lateinit var daggerAppComponent: AppComponent

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        daggerAppComponent = DaggerAppComponent.builder().application(this).build()
        daggerAppComponent.inject(this)
        super.onCreate()
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}