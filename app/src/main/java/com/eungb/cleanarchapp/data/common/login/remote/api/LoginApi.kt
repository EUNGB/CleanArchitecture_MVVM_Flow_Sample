package com.eungb.cleanarchapp.data.common.login.remote.api

import com.eungb.cleanarchapp.data.common.login.remote.dto.LoginRequest
import com.eungb.cleanarchapp.data.common.login.remote.dto.LoginResponse
import com.eungb.cleanarchapp.data.common.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<WrappedResponse<LoginResponse>>

}