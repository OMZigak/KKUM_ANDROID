package com.teamkkumul.core.data.di

import com.teamkkumul.core.data.repository.AuthRepository
import com.teamkkumul.core.data.repository.HomeRepository
import com.teamkkumul.core.data.repository.MeetUpCreateLocationRepository
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.data.repository.MeetingsRepository
import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.core.data.repository.ProfileRepository
import com.teamkkumul.core.data.repository.ReqresRepository
import com.teamkkumul.core.data.repository.UserInfoRepository
import com.teamkkumul.core.data.repositoryimpl.AuthRepositoryImpl
import com.teamkkumul.core.data.repositoryimpl.HomeRepositoryImpl
import com.teamkkumul.core.data.repositoryimpl.MeetUpCreateLocationRepositoryImpl
import com.teamkkumul.core.data.repositoryimpl.MeetUpRepositoryImpl
import com.teamkkumul.core.data.repositoryimpl.MeetingsRepositoryImpl
import com.teamkkumul.core.data.repositoryimpl.MyGroupRepositoryImpl
import com.teamkkumul.core.data.repositoryimpl.ProfileRepositoryImpl
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
        repository: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsMyGroupRepo(
        repository: MyGroupRepositoryImpl,
    ): MyGroupRepository

    @Binds
    @Singleton
    abstract fun bindsMeetUpGroupParticipant(
        repository: MeetUpRepositoryImpl,
    ): MeetUpRepository

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

    @Binds
    @Singleton
    abstract fun bindsHomeRepo(
        repository: HomeRepositoryImpl,
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindsMeetUpCreateRepo(
        repository: MeetUpCreateLocationRepositoryImpl,
    ): MeetUpCreateLocationRepository
}
