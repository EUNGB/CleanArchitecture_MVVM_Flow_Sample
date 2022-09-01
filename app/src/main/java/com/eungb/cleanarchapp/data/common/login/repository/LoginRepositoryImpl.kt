package com.eungb.cleanarchapp.data.common.login.repository

import com.eungb.cleanarchapp.data.common.login.remote.api.LoginApi
import com.eungb.cleanarchapp.data.common.login.remote.dto.LoginRequest
import com.eungb.cleanarchapp.data.common.login.remote.dto.LoginResponse
import com.eungb.cleanarchapp.data.common.utils.WrappedListResponse
import com.eungb.cleanarchapp.data.common.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.login.LoginRepository
import com.eungb.cleanarchapp.domain.login.entity.LoginEntity
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginApi: LoginApi) : LoginRepository {

    override suspend fun login(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>> {
        return flow {
            val response = loginApi.login(loginRequest)
            if (response.isSuccessful) {
                val body = response.body()!!
                val loginEntity = LoginEntity(
                    body.data?.id!!,
                    body.data?.name!!,
                    body.data?.email!!,
                    body.data?.token!!
                )
                emit(BaseResult.Success(loginEntity))
            } else {
                val type = object : TypeToken<WrappedResponse<LoginResponse>>() {}.type
                val err: WrappedResponse<LoginResponse> = Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

}