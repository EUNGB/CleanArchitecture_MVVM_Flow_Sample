package com.eungb.cleanarchapp.di

import com.eungb.cleanarchapp.BuildConfig
import com.eungb.cleanarchapp.data.utils.RequestInterceptor
import com.eungb.cleanarchapp.infra.utils.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModules {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
//            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(okHttpClient)
            baseUrl(BuildConfig.API_BASE_URL)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(requestInterceptor: RequestInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
            addInterceptor(loggingInterceptor)
        }.build()
    }

    @Provides
    fun provideRequestInterceptor(pref: SharedPrefs): RequestInterceptor = RequestInterceptor(pref)

}