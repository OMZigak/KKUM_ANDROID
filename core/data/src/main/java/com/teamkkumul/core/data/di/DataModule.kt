package com.teamkkumul.core.data.di

import com.teamkkumul.core.data.repository.MeetingsRepository
import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.data.repository.LoginRepository
import com.teamkkumul.core.data.repository.ReqresRepository
import com.teamkkumul.core.data.repository.UserInfoRepository
import com.teamkkumul.core.data.repositoryimpl.MeetingsRepositoryImpl
import com.teamkkumul.core.data.repositoryimpl.ProfileRepositoryImpl
import com.teamkkumul.core.data.repositoryimpl.LoginRepositoryImpl
import com.teamkkumul.core.data.repositoryimpl.ReqresRepositoryImpl
import com.teamkkumul.core.data.repositoryimpl.UserInfoRepositoryImpl
import com.teamkkumul.core.datastore.datasource.DefaultKumulPreferenceDatasource
import com.teamkkumul.core.datastore.datasource.KumulPreferencesDataSource
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
    abstract fun bindsReqresRepo(
        reqresRepository: ReqresRepositoryImpl,
    ): ReqresRepository

    @Binds
    @Singleton
    abstract fun bindsKumulLocalDataSource(
        dataSource: DefaultKumulPreferenceDatasource,
    ): KumulPreferencesDataSource

    @Binds
    @Singleton
    abstract fun bindsUserInfoRepo(
        repository: UserInfoRepositoryImpl,
    ): UserInfoRepository

    @Binds
    @Singleton
    abstract fun bindsLoginRepo(
        repository: LoginRepositoryImpl,
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindsMeetingsRepo(
        meetingsRepository: MeetingsRepositoryImpl,
    ): MeetingsRepository

    @Binds
    @Singleton
    abstract fun bindsProfileRepo(
        profileRepository: ProfileRepositoryImpl,
    ): ProfileRepository
}
