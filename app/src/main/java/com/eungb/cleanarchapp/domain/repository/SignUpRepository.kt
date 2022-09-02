package com.eungb.cleanarchapp.domain.repository

import com.eungb.cleanarchapp.data.remote.dto.SignUpRequest
import com.eungb.cleanarchapp.data.remote.dto.SignUpResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.LoginEntity
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {

    suspend fun signUp(signUpRequest: SignUpRequest) : Flow<BaseResult<LoginEntity, WrappedResponse<SignUpResponse>>>
}