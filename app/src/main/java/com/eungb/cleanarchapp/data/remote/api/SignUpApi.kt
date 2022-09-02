package com.eungb.cleanarchapp.data.remote.api

import com.eungb.cleanarchapp.data.remote.dto.SignUpRequest
import com.eungb.cleanarchapp.data.remote.dto.SignUpResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {

    @POST("/api/auth/register")
    suspend fun signUp(
        @Body singUpRequest: SignUpRequest
    ): Response<WrappedResponse<SignUpResponse>>
}