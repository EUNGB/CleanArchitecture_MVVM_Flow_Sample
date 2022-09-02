package com.eungb.cleanarchapp.di

import com.eungb.cleanarchapp.data.remote.api.LoginApi
import com.eungb.cleanarchapp.data.remote.api.ProductApi
import com.eungb.cleanarchapp.data.remote.api.SignUpApi
import com.eungb.cleanarchapp.data.remote.datasource.ProductDataSource
import com.eungb.cleanarchapp.data.remote.datasource.ProductDataSourceImpl
import com.eungb.cleanarchapp.data.remote.datasource.SignUpDataSource
import com.eungb.cleanarchapp.data.remote.datasource.SignUpDataSourceImpl
import com.eungb.cleanarchapp.data.repository.LoginRepositoryImpl
import com.eungb.cleanarchapp.data.repository.ProductRepositoryImpl
import com.eungb.cleanarchapp.data.repository.SignUpRepositoryImpl
import com.eungb.cleanarchapp.domain.repository.LoginRepository
import com.eungb.cleanarchapp.domain.repository.ProductRepository
import com.eungb.cleanarchapp.domain.repository.SignUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [NetworkModules::class])
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // DataSource
    @Singleton
    @Provides
    fun provideSignUpDataSource(signUpApi: SignUpApi): SignUpDataSource = SignUpDataSourceImpl(signUpApi)

    @Singleton
    @Provides
    fun provideProductDataSource(productApi: ProductApi): ProductDataSource = ProductDataSourceImpl(productApi)

    // Repository
    @Singleton
    @Provides
    fun provideLoginRepository(loginApi: LoginApi): LoginRepository = LoginRepositoryImpl(loginApi)

    @Singleton
    @Provides
    fun provideSignUpRepository(dataSource: SignUpDataSource): SignUpRepository = SignUpRepositoryImpl(dataSource)

    @Singleton
    @Provides
    fun provideProductRepository(dataSource: ProductDataSource): ProductRepository = ProductRepositoryImpl(dataSource)

}