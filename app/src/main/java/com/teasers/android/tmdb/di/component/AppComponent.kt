package com.teasers.android.tmdb.di.component

import android.app.Application
import com.teasers.android.tmdb.TmdbApplication
import com.teasers.android.tmdb.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RetrofitModule::class, RepositoryModule::class,
        ActivityModule::class, FragmentModule::class,
        ViewModelModule::class, AndroidSupportInjectionModule::class]
)
interface AppComponent {

    fun inject(app: TmdbApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}