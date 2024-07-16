package com.teamkkumul.core.network.di

import com.teamkkumul.core.network.api.MyGroupService
import com.teamkkumul.core.network.api.LoginService
import com.teamkkumul.core.network.api.ReqresService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
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
}
