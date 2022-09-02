package com.eungb.cleanarchapp.data.remote.utils

import com.eungb.cleanarchapp.data.utils.WrappedListResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

object RemoteResponse {

    fun <T> getResponseError(response: Response<WrappedResponse<T>>): WrappedResponse<T> {
        val type = object : TypeToken<WrappedResponse<T>>() {}.type
        val err: WrappedResponse<T> = Gson().fromJson(response.errorBody()!!.charStream(), type)
        err.code = response.code()
        return err
    }

    fun <T> getListResponseError(response: Response<WrappedListResponse<T>>): WrappedListResponse<T> {
        val type = object : TypeToken<WrappedListResponse<T>>() {}.type
        val err: WrappedListResponse<T> = Gson().fromJson(response.errorBody()!!.charStream(), type)
        err.code = response.code()
        return err
    }
}