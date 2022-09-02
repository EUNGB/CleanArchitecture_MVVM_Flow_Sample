package com.eungb.cleanarchapp.domain.usecase

import com.eungb.cleanarchapp.data.remote.dto.LoginRequest
import com.eungb.cleanarchapp.data.remote.dto.LoginResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.repository.LoginRepository
import com.eungb.cleanarchapp.domain.entity.LoginEntity
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