package com.teamkkumul.core.network.di

import com.teamkkumul.core.network.api.MeetingsService
import com.teamkkumul.core.network.api.ProfileService
import com.teamkkumul.core.network.api.HomeService
import com.teamkkumul.core.network.api.LoginService
import com.teamkkumul.core.network.api.MeetUpService
import com.teamkkumul.core.network.api.MeetingsService
import com.teamkkumul.core.network.api.MyGroupService
import com.teamkkumul.core.network.api.ReqresService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideReqresService(@KKUMUL retrofit: Retrofit): ReqresService =
        retrofit.create(ReqresService::class.java)

    @Provides
    @Singleton
    fun provideMyGroupService(@KKUMUL retrofit: Retrofit): MyGroupService =
        retrofit.create(MyGroupService::class.java)

    @Provides
    @Singleton
    fun provideLoginService(@WithoutTokenInterceptor retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideMeetUpService(@KKUMUL retrofit: Retrofit): MeetUpService =
        retrofit.create(MeetUpService::class.java)

    @Provides
    @Singleton
    fun provideMeetingsService(@KKUMUL retrofit: Retrofit): MeetingsService =
        retrofit.create(MeetingsService::class.java)

    @Provides
    @Singleton
    fun provideProfileService(@KKUMUL retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)

    @Provides
    @Singleton
    fun provideHomeService(@KKUMUL retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)
}
