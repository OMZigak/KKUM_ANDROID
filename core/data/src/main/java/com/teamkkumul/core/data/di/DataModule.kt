package com.teamkkumul.core.data.di

import com.teamkkumul.core.data.repository.ReqresRepository
import com.teamkkumul.core.data.repositoryimpl.ReqresRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindsReqresRepo(reqresRepository: ReqresRepositoryImpl): ReqresRepository
}
