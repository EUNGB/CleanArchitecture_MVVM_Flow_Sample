package com.eungb.cleanarchapp.domain.usecase

import com.eungb.cleanarchapp.data.remote.dto.SignUpRequest
import com.eungb.cleanarchapp.data.remote.dto.SignUpResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.LoginEntity
import com.eungb.cleanarchapp.domain.repository.SignUpRepository
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: SignUpRepository
) {

    suspend fun invoke(signUpRequest: SignUpRequest) : Flow<BaseResult<LoginEntity, WrappedResponse<SignUpResponse>>> {
        return repository.signUp(signUpRequest)
    }
}