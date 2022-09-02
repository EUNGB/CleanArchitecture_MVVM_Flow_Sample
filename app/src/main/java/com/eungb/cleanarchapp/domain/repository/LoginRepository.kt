package com.eungb.cleanarchapp.domain.repository

import com.eungb.cleanarchapp.data.remote.dto.LoginRequest
import com.eungb.cleanarchapp.data.remote.dto.LoginResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.LoginEntity
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun login(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>>
}