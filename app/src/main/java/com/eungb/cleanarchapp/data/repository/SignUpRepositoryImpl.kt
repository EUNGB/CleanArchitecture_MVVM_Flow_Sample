package com.eungb.cleanarchapp.data.repository

import com.eungb.cleanarchapp.data.remote.datasource.SignUpDataSource
import com.eungb.cleanarchapp.data.remote.dto.SignUpRequest
import com.eungb.cleanarchapp.data.remote.dto.SignUpResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.LoginEntity
import com.eungb.cleanarchapp.domain.repository.SignUpRepository
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignUpRepositoryImpl(
    private val dataSource: SignUpDataSource
) : SignUpRepository {

    override suspend fun signUp(signUpRequest: SignUpRequest): Flow<BaseResult<LoginEntity, WrappedResponse<SignUpResponse>>> {
        return flow {
            emit(dataSource.signUp(signUpRequest))
        }
    }
}