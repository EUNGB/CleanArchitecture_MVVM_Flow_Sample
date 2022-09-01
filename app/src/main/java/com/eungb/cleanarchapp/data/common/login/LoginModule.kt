package com.eungb.cleanarchapp.data.common.login

import com.eungb.cleanarchapp.data.common.login.remote.api.LoginApi
import com.eungb.cleanarchapp.data.common.login.repository.LoginRepositoryImpl
import com.eungb.cleanarchapp.data.common.module.NetworkModules
import com.eungb.cleanarchapp.domain.login.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModules::class])
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit) : LoginApi = retrofit.create(LoginApi::class.java)

    @Singleton
    @Provides
    fun provideLoginRepository(loginApi: LoginApi): LoginRepository = LoginRepositoryImpl(loginApi)

}