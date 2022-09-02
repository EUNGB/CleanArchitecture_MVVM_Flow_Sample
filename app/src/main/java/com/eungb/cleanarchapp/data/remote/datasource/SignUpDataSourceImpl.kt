package com.eungb.cleanarchapp.data.remote.datasource

import com.eungb.cleanarchapp.data.remote.api.SignUpApi
import com.eungb.cleanarchapp.data.remote.dto.SignUpRequest
import com.eungb.cleanarchapp.data.remote.dto.SignUpResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.LoginEntity
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SignUpDataSourceImpl(
    private val signUpApi: SignUpApi
) : SignUpDataSource {

    override suspend fun signUp(signUpRequest: SignUpRequest): BaseResult<LoginEntity, WrappedResponse<SignUpResponse>> {
        val response = signUpApi.signUp(signUpRequest)
        return if (response.isSuccessful) {
            val body = response.body()!!
            val signUpEntity = LoginEntity(
                body.data?.id!!,
                body.data?.name!!,
                body.data?.email!!,
                body.data?.token!!
            )
            BaseResult.Success(signUpEntity)
        } else {
            val type = object : TypeToken<WrappedResponse<SignUpResponse>>() {}.type
            val err: WrappedResponse<SignUpResponse> = Gson().fromJson(response.errorBody()!!.charStream(), type)
            err.code = response.code()
            BaseResult.Error(err)
        }
    }

}