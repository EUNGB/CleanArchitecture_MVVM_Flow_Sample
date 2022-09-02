package com.eungb.cleanarchapp.data.remote.datasource

import com.eungb.cleanarchapp.data.remote.dto.SignUpRequest
import com.eungb.cleanarchapp.data.remote.dto.SignUpResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.LoginEntity
import com.eungb.cleanarchapp.presentation.common.base.BaseResult

interface SignUpDataSource {

    suspend fun signUp(signUpRequest: SignUpRequest): BaseResult<LoginEntity, WrappedResponse<SignUpResponse>>
}