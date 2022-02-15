package com.teasers.android.tmdb.di.module

import com.teasers.android.tmdb.data.MovieRepositoryImpl
import com.teasers.android.tmdb.data.Repository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repositoryImpl: MovieRepositoryImpl): Repository

}