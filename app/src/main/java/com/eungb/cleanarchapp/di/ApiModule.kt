package com.eungb.cleanarchapp.di

import com.eungb.cleanarchapp.data.remote.api.LoginApi
import com.eungb.cleanarchapp.data.remote.api.ProductApi
import com.eungb.cleanarchapp.data.remote.api.SignUpApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit): LoginApi = retrofit.create(LoginApi::class.java)

    @Singleton
    @Provides
    fun provideSignUpApi(retrofit: Retrofit): SignUpApi = retrofit.create(SignUpApi::class.java)

    @Singleton
    @Provides
    fun provideProductApi(retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)

}