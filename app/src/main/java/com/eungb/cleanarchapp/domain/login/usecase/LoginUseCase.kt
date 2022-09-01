package com.eungb.cleanarchapp.domain.login.usecase

import com.eungb.cleanarchapp.data.common.login.remote.dto.LoginRequest
import com.eungb.cleanarchapp.data.common.login.remote.dto.LoginResponse
import com.eungb.cleanarchapp.data.common.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.login.LoginRepository
import com.eungb.cleanarchapp.domain.login.entity.LoginEntity
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend fun invoke(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>> {
        return loginRepository.login(loginRequest)
    }
}