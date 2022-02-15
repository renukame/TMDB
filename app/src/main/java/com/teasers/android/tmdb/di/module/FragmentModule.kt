package com.teasers.android.tmdb.di.module

import com.teasers.android.tmdb.ui.fragment.DetailFragment
import com.teasers.android.tmdb.ui.fragment.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeListFragment(): ListFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment
}