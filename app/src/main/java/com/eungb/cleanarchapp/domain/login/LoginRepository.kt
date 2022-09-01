package com.eungb.cleanarchapp.domain.login

import com.eungb.cleanarchapp.data.common.login.remote.dto.LoginRequest
import com.eungb.cleanarchapp.data.common.login.remote.dto.LoginResponse
import com.eungb.cleanarchapp.data.common.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.login.entity.LoginEntity
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun login(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>>
}