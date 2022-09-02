package com.eungb.cleanarchapp.data.remote.api

import com.eungb.cleanarchapp.data.remote.dto.LoginRequest
import com.eungb.cleanarchapp.data.remote.dto.LoginResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<WrappedResponse<LoginResponse>>

}