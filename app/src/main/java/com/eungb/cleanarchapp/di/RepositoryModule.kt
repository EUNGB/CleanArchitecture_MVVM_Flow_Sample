package com.eungb.cleanarchapp.di

import com.eungb.cleanarchapp.data.remote.api.LoginApi
import com.eungb.cleanarchapp.data.remote.api.SignUpApi
import com.eungb.cleanarchapp.data.remote.datasource.SignUpDataSource
import com.eungb.cleanarchapp.data.remote.datasource.SignUpDataSourceImpl
import com.eungb.cleanarchapp.data.repository.LoginRepositoryImpl
import com.eungb.cleanarchapp.data.repository.SignUpRepositoryImpl
import com.eungb.cleanarchapp.domain.repository.LoginRepository
import com.eungb.cleanarchapp.domain.repository.SignUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import javax.sql.DataSource


@Module(includes = [NetworkModules::class])
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // DataSource
    @Singleton
    @Provides
    fun provideSignUpDataSource(signUpApi: SignUpApi): SignUpDataSource = SignUpDataSourceImpl(signUpApi)


    // Repository
    @Singleton
    @Provides
    fun provideLoginRepository(loginApi: LoginApi): LoginRepository = LoginRepositoryImpl(loginApi)

    @Singleton
    @Provides
    fun provideSignUpRepository(dataSource: SignUpDataSource): SignUpRepository = SignUpRepositoryImpl(dataSource)

}